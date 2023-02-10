package com.example.appstore

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.fonts.FontStyle
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    private val _isDownloadComplete = MutableStateFlow(false)
    private val isDownloadComplete = _isDownloadComplete.asStateFlow()

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
            _isDownloadComplete.value = true
            downloadController.enqueueDownload {
                _isDownloadComplete.value = false
            }
        } else {
            Log.d("PERMISSIONS", "[PERMISSIONS]All is not granted!")
        }
    }

    private val packageInstallerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (isQCompatibility()) {
            if (packageManager.canRequestPackageInstalls()) {
                Log.d("AppStoreSample", "canRequestPackageInstalls success")
                Toast.makeText(this, "Unknown App Source granted!!", Toast.LENGTH_SHORT).show()
                downloadController.installAssetApk()
            }
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
                    InstallApkScreen(getString(R.string.appstore), isCopyComplete, isDownloadComplete) { installType ->
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
            runButtonTest(installType)
        }
    }
    
    private fun runButtonTest(installType: String) {
        when(installType) {
            "download" -> {
                CoroutineScope(Dispatchers.IO).launch {
                    _isDownloadComplete.value = true
                    downloadController.enqueueDownload {
                        _isDownloadComplete.value = false
                    }

                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, getString(R.string.downloading), Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
            "asset" -> {
                if (isQCompatibility()) {
                    requestUnknownAppSource()
                }
            }
            "PackageInstaller" -> {
                requestNoUnknownSource()
            }
            "UnInstall Starbucks" -> {
                requestUnInstallStarbucks()
            }
            "UnInstall Memo" -> {
                requestUnInstallMemo()
            }
            else -> error("unknown install type")
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

        if (!File(outPath).exists()) {
            Toast.makeText(this@MainActivity, getString(R.string.copy_asset_to_file), Toast.LENGTH_LONG)
                .show()
            CoroutineScope(Dispatchers.IO).launch {
                copyApkToAppFolder(outPath) {
                    _isCopyComplete.value = true
                }
            }
        } else {
            _isCopyComplete.value = true
        }
    }

    private fun copyApkToAppFolder(outPath: String, onComplete: () -> Unit) {
        val inputStream = assets.open("app.apk")
        Log.d("AppStoreSample", "copyApkToAppFolder() outPath:$outPath")

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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun requestUnknownAppSource() {
        if (packageManager.canRequestPackageInstalls()) {
            Log.e("AppStoreSample", "Already UnKnown App Source")
            Toast.makeText(this, "Already UnKnown App Source", Toast.LENGTH_SHORT).show()
            //이미 permission 있으면, install 로직 구현
            downloadController.installAssetApk()
            return
        }

        //permission 필요 하면, 퍼미션 설정 화면 요청
        //setData 를 하지 않으면, unknown app 관련 요청 앱들 리스트가 보인 상태에서 해당 패키지앱 선택하도록 하고,
        //package 전달 하면 해당앱을 선택 하여 스위치 버튼이 나오는 설정앱이 나옴
        //설정이 끝나면, packageInstallerLauncher 에서 다시 한번 canRequestPackageInstalls 확인 하여 다음 액션을 취함
        Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
            .setData(
                Uri.parse("package:$packageName")
            ).run {
                packageInstallerLauncher.launch(this)
            }
    }

    private fun requestNoUnknownSource() {
        val inputStream = assets.open("app.apk")
        downloadController.installApkNoUnknownSource(inputStream) {
            inputStream.close()
        }
    }

    private fun requestUnInstallMemo() {
        downloadController.uninstallAssetADownloadApp()

    }

    private fun requestUnInstallStarbucks() {
        downloadController.uninstallServerDownloadApp()
    }
}

@Composable
fun InstallApkScreen(
    name: String,
    downloadState: StateFlow<Boolean> ,
    downloadStateFromServer: StateFlow<Boolean> ,
    onButtonCallback: (String) -> Unit
) {
    val enableState by downloadState.collectAsState()
    val loadingState by downloadStateFromServer.collectAsState()

    Box(modifier = Modifier) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$name Test!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h2
            )

            Spacer(modifier = Modifier.padding(10.dp))

            //server 에서 download 받아서 앱 설치 실행
            Button(onClick = { onButtonCallback("download") }) {
                Text("Install Starbucks from Server")
            }

            Spacer(modifier = Modifier.padding(5.dp))

            //asset 을 app file 로 copy 해서 앱 설치 실행
            Button(onClick = { onButtonCallback("asset") }, enabled = enableState) {
                Text("Install Memo from Asset[FileProvider]")
            }

            Spacer(modifier = Modifier.padding(5.dp))

            //asset 을 app file 로 copy 해서 앱 설치 실행
            Button(onClick = { onButtonCallback("PackageInstaller") }, enabled = enableState) {
                Text("Install Memo from Asset[Package Installer]")
            }

            Spacer(modifier = Modifier.padding(5.dp))

            //asset 을 app file 로 copy 해서 앱 설치 실행
            Button(onClick = { onButtonCallback("UnInstall Starbucks") }) {
                Text("UnInstall Starbucks")
            }

            Spacer(modifier = Modifier.padding(5.dp))

            //asset 을 app file 로 copy 해서 앱 설치 실행
            Button(onClick = { onButtonCallback("UnInstall Memo") }) {
                Text("UnInstall Memo")
            }
        }
        if (loadingState) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(size = 64.dp)
                    .align(Alignment.Center),
                color = Color.Magenta,
                strokeWidth = 10.dp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val _isCopyComplete = MutableStateFlow(false)
    val isCopyComplete = _isCopyComplete.asStateFlow()

    val _isDownloadComplete = MutableStateFlow(false)
    val isDownloadComplete = _isDownloadComplete.asStateFlow()

    AppStoreSampleTheme {
        InstallApkScreen(stringResource(id = R.string.appstore), isCopyComplete, isDownloadComplete) {}
    }
}