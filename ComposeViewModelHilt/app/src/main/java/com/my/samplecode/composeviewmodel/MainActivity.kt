package com.my.samplecode.composeviewmodel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.my.samplecode.composeviewmodel.ui.screen.MyScreen
import com.my.samplecode.composeviewmodel.ui.theme.ComposeViewModelTheme
import com.my.samplecode.composeviewmodel.viewmodel.MyViewModel
import dagger.hilt.android.AndroidEntryPoint

//sample code : (https://medium.com/codex/best-architecture-for-jetpack-compose-45d2fc82aa92)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    // implementation "androidx.hilt:hilt-navigation-compose:1.0.0" 쓰면 이렇게 하지않아도 됨
    // hiltViewModel() 바로 주입할수 있음
    private val myViewModel: MyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeViewModelTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //Greeting("Android")
                    MyScreen()
                }
            }
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
    ComposeViewModelTheme {
        Greeting("Android")
    }
}