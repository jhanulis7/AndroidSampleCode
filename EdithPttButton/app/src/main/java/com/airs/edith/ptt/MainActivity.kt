package com.airs.edith.ptt

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.airs.edith.ptt.service.EdithService
import com.airs.edith.ptt.ui.theme.EdithPttButtonTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestSystemPermission()

        setContent {
            EdithPttButtonTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }

    /** Defines callbacks for service binding, passed to bindService()  */
    private lateinit var edithService: EdithService
    private var edithBound: Boolean = false
    private val edithConnection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            Log.d("MainActivity", "onServiceConnected()")
            val binder = service as EdithService.LocalBinder
            edithService = binder.getSEdithService()
            edithBound = true
            showToast()
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            Log.d("MainActivity", "onServiceDisconnected()")
            edithBound = false
        }
    }

    private fun connectEdithService() {
        // Bind to Edith LocalService
        Intent(this, EdithService::class.java).also { intent ->
            bindService(intent, edithConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("MainActivity", "onStart()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity", "onDestroy()")
        unbindService(edithConnection)
        edithBound = false
    }

    private val requestSystemAlertWindowActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()  // ◀ StartActivityForResult 처리를 담당
    ) {
        Log.d("MainActivity", "resultCode : ${it.resultCode} ${Activity.RESULT_OK}")
        requestSystemPermission()
    }

    private fun requestSystemPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestOverlayPermission()
        } else {
            connectEdithService()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestOverlayPermission() {
        if (Settings.canDrawOverlays(this)) {
            connectEdithService()
        } else {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            requestSystemAlertWindowActivity.launch(intent)
        }
    }

    /** Called when a button is clicked (the button in the layout file attaches to
     * this method with the android:onClick attribute)  */
    fun showToast() {
        if (edithBound) {
            // Call a method from the LocalService.
            // However, if this call were something that might hang, then this request should
            // occur in a separate thread to avoid slowing down the activity performance.
            val num: Int = edithService.randomNumber
            Log.d("showToast", "number: $num")
            Toast.makeText(this, "number: $num", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    EdithPttButtonTheme {
        Greeting("Android")
    }
}


