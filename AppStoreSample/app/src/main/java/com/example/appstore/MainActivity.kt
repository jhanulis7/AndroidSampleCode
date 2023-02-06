package com.example.appstore

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.example.appstore.ui.theme.AppStoreSampleTheme

//reference : https://androidwave.com/download-and-install-apk-programmatically/
//https://codechacha.com/ko/how-to-install-and-uninstall-app-in-android/
class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.entries.all {
            Log.d("PERMISSIONS", "${it.key} = ${it.value}")
            it.value
        }
        if (granted) {
            // navigate to respective screen
            Log.d("PERMISSIONS", "[PERMISSIONS] All is granted")
            downloadController.enqueueDownload()
        } else {
            Log.d("PERMISSIONS", "[PERMISSIONS]All is not granted!")
        }
    }


    companion object {
        const val PERMISSION_REQUEST_STORAGE = 0
    }
    lateinit var downloadController: DownloadController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This apk is taking pagination sample app
        val apkUrl = "https://edith-android.s3.ap-northeast-2.amazonaws.com/EdithAgentTsd.apk?versionId=imI5n4juuxlbv_xPQHpdnGT0Yl0L2blQ"
        downloadController = DownloadController(this, apkUrl)

        setContent {
            AppStoreSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Download Sample Test\"") {
                        requestPermission()
                    }
                }
            }
        }
    }

    private fun showPermissionDialog(message: String, doNotAskAgain: Boolean) {
        val alertDialogBuilder = AlertDialog.Builder(this)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(getString(R.string.confirm_button_text)) { _, _ ->
                if (doNotAskAgain) {
                    try {
                        //Open the specific App Info page:
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.parse("package:$packageName")
                        }.also { startActivity(it) }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        //Open the generic Apps page:
                        Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS).apply {
                            data = Uri.parse("package:$packageName")
                        }.also { startActivity(it) }
                    }
                } else {
                    val permissions = ArrayList<String>()

                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                        if (ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        }
                    }

                    var arrPermissions: Array<String?> = arrayOfNulls(permissions.size)
                    arrPermissions = permissions.toArray(arrPermissions)
                    if (permissions.isNotEmpty()) {
                        requestPermissionLauncher.launch(permissions.toArray(arrPermissions))
                    }
                }
            }
        alertDialogBuilder.show()
    }


    private fun requestPermission() {
        var doNotAskAgain = false
        //		\n\n- 위치\n- 전화\n- 저장\n
        var needPermissions = ""

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                needPermissions += getString(R.string.storage_access_required)
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    doNotAskAgain = true
                }
            }
        }

        var message = needPermissions
        if (doNotAskAgain) {
            message += getString(R.string.str_msg_permission_attach)
        }
        if (message.isNotEmpty()) {
            showPermissionDialog(message, doNotAskAgain)
        } else {
            downloadController.enqueueDownload()
        }
    }

}

@Composable
fun Greeting(name: String, onCheckStoragePermission: () -> Unit) {
    Column() {
        Text(text = "Hello $name!")

        Button(onClick = { onCheckStoragePermission() }) {
            
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppStoreSampleTheme {
        Greeting("Download Sample Test") {}
    }
}