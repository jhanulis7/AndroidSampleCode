package com.my.samplecode.kotlincoroutineretrofitmvvm.retrofit

class MainRepository constructor(private val retrofitService: RetrofitService) {
    suspend fun getAllMovies() = retrofitService.getAllMovies()
}