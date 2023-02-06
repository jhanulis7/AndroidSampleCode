package com.example.appstore

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.example.appstore.ui.theme.AppStoreSampleTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

//reference : https://androidwave.com/download-and-install-apk-programmatically/
//https://codechacha.com/ko/how-to-install-and-uninstall-app-in-android/
class MainActivity : ComponentActivity() {
    private lateinit var downloadController: DownloadController
    private val _isCopyComplete = MutableStateFlow(false)
    private val isCopyComplete = _isCopyComplete.asStateFlow()
    
    private val requestInstallerActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()  // ◀ StartActivityForResult 처리를 담당
    ) {
        Log.d("MainActivity", "resultCode : ${it.resultCode} ${Activity.RESULT_OK}")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (packageManager.canRequestPackageInstalls()) {
                Log.d("MainActivity", "canRequestPackageInstalls success")
                downloadController.assetInstallApk()
            }
        } else {
            Log.d("MainActivity", "canRequestPackageInstalls fail")
            downloadController.assetInstallApk()
        }
    }
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initServerDownload()
        initAssetCopyToApp()

        setContent {
            AppStoreSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    InstallApkScreen(getString(R.string.appstore), isCopyComplete) { installType ->
                        requestPermission(installType)
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


    private fun requestPermission(installType: String) {
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
            val permissions = java.util.ArrayList<String>()

            when(installType) {
                "download" -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        downloadController.enqueueDownload()

                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@MainActivity, getString(R.string.downloading), Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
                "asset" -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        //downloadController.assetInstallApk()
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            if (packageManager.canRequestPackageInstalls()) {
                                downloadController.assetInstallApk()
                            } else {
                                getInstallApp()
                            }
                        } else {
                            getInstallApp()
                        }
                    }
                }
                else -> error("unknown install type")
            }
        }
    }

    private fun initServerDownload() {
        //starbucks sample apk
        val apkUrl = "https://d.apkpure.com/b/APK/com.starbucks.co?version=latest"
        downloadController = DownloadController(this, apkUrl)
    }
    private fun initAssetCopyToApp() {
        //for asset installer
        val outPath= filesDir.absolutePath + "/app.apk"
        CoroutineScope(Dispatchers.IO).launch {
            if (!File(outPath).exists()) {
                copyApkToAppFolder(outPath) {
                    _isCopyComplete.value = true
                }
            } else {
                _isCopyComplete.value = true
            }
        }
        Toast.makeText(this@MainActivity, getString(R.string.copy_asset_to_file), Toast.LENGTH_LONG)
            .show()
    }

    private fun copyApkToAppFolder(outPath: String, onComplete: () -> Unit) {
        val inputStream = assets.open("app.apk")
        Log.d("Installer", "copyApkToAppFolder() outPath:$outPath")

        val outputStream = FileOutputStream(outPath)
        while (true) {
            val data = inputStream.read()
            if (data == -1) {
                break
            }
            outputStream.write(data)
        }
        inputStream.close()
        outputStream.close()
        onComplete()
    }

    //TODO [안드로이드 8.0 이상 출처를 알수 없는 앱 설치 허용 설정창 이동 메소드]
    fun getInstallApp() {
        // 안드로이드 8.0 이상 출처를 알 수 없는 앱 설정 화면 이동
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:$packageName"))
            intent.data = Uri.parse("package:$packageName");
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            //startActivity(intent)
            requestInstallerActivity.launch(intent)
        }
    }

}

@Composable
fun InstallApkScreen(
    name: String,
    downloadState: StateFlow<Boolean> ,
    onButtonCallback: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val enableState by downloadState.collectAsState()
        Text(text = "$name Test!", style = MaterialTheme.typography.h2)

        Spacer(modifier = Modifier.padding(20.dp))

        //server 에서 download 받아서 앱 설치 실행
        Button(onClick = { onButtonCallback("download") }) {
            Text("Server 에서 Apk Download 받아서 설치 실행")
        }

        Spacer(modifier = Modifier.padding(20.dp))

        //asset 을 app file 로 copy 해서 앱 설치 실행
        Button(onClick = { onButtonCallback("asset") }, enabled = enableState) {
            Text("Asset 에서 Apk Download 받아서 설치 실행")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val _isCopyComplete = MutableStateFlow(false)
    val isCopyComplete = _isCopyComplete.asStateFlow()

    AppStoreSampleTheme {
        InstallApkScreen(stringResource(id = R.string.appstore), isCopyComplete) {}
    }
}