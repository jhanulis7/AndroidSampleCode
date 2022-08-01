package com.my.samplecode.kakaologin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.my.samplecode.kakaologin.ui.theme.KakaoLoginTheme

class MainActivity : ComponentActivity() {
    private val kakaoLoginViewModel: KakaoLoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KakaoLoginTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    kakaoLoginScreen(kakaoLoginViewModel)
                }
            }
        }
    }
}

@Composable
fun kakaoLoginScreen(
    viewModel: KakaoLoginViewModel
) {
    val isLoggedIn = viewModel.isLoginFlow.collectAsState()
    val isLoggedInText = if (isLoggedIn.value) "카카오 로그인 상태" else "카카오 로그아웃 상태"

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Spacer(Modifier.height(10.dp))
        Button(onClick = {
            viewModel.logInKakao()
        }) {
            Text(text = "카카오 로그인하기")
        }
        Spacer(Modifier.height(10.dp))
        Button(onClick = {
            viewModel.logOutKakao()
        }) {
            Text(text = "카카오 로그아웃하기")
        }
        Spacer(Modifier.height(10.dp))
        Text(
            text = isLoggedInText,
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
    }
}


@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    KakaoLoginTheme {
        Greeting("Android")
    }
}