package com.my.samplecode.kotlincoroutineretrofitmvvm.retrofit

import com.my.samplecode.kotlincoroutineretrofitmvvm.model.Movie
import retrofit2.Response
import retrofit2.http.GET

interface RetrofitService {
    @GET("movielist.json")
    suspend fun getAllMovies() : Response<List<Movie>>
}