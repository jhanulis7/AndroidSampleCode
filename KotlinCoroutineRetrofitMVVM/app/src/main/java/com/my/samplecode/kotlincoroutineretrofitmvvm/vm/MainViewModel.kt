package com.my.samplecode.kotlincoroutineretrofitmvvm.vm

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.my.samplecode.kotlincoroutineretrofitmvvm.model.Movie
import com.my.samplecode.kotlincoroutineretrofitmvvm.retrofit.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepository: MainRepository): ViewModel() {
    private val _movieListResponse = mutableStateOf<List<Movie>>(listOf())
    private val errorMessage = mutableStateOf("")
    var movieList:List<Movie> by mutableStateOf(listOf())
    var job: Job? = null

    fun fetchMovies() {
        job = viewModelScope.launch {
            Log.d("TEST", "fetchMovies")
            val response = mainRepository.getAllMovies()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Log.d("TEST", "fetchMovies isSuccessful")
                    response.body()?.let {
                        movieList = it
                    }
                } else {
                    Log.d("TEST", "fetchMovies Error")
                    onError("Error : ${response.message()} ")
                }
            }
        }
    }

    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    private fun onError(message: String) {
        Log.d("TEST", "onError")
        errorMessage.value = message
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}