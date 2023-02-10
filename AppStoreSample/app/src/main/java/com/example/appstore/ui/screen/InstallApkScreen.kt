package com.example.appstore.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appstore.ui.theme.AppStoreSampleTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.appstore.R

@Composable
fun InstallApkScreen(
    name: String,
    downloadState: StateFlow<Boolean>,
    downloadStateFromServer: StateFlow<Boolean>,
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