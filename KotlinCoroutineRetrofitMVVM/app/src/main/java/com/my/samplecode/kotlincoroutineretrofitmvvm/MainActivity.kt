package com.my.samplecode.kotlincoroutineretrofitmvvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.my.samplecode.kotlincoroutineretrofitmvvm.ui.screen.MovieScreen
import com.my.samplecode.kotlincoroutineretrofitmvvm.ui.theme.KotlinCoroutineRetrofitMVVMTheme
import com.my.samplecode.kotlincoroutineretrofitmvvm.vm.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

//reference : https://medium.com/android-beginners/mvvm-with-kotlin-coroutines-and-retrofit-example-d3f5f3b09050
// https://howtodoandroid.com/jetpack-compose-retrofit-recyclerview/

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotlinCoroutineRetrofitMVVMTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    //val mainViewModel: MainViewModel = hiltViewModel()
                    MovieScreen(mainViewModel)
                    mainViewModel.fetchMovies()
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
    KotlinCoroutineRetrofitMVVMTheme {
        Greeting("Android")
    }
}