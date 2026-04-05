package com.example.krantikari

import com.example.krantikari.models.User
import retrofit2.Response
import retrofit2.http.GET

interface Api {
    // This appends to the Chuck Norris base URL
    @GET("jokes/random")
    suspend fun getJokes(): Response<Jokes>

    // This appends to the Random User base URL
    @GET("api/")
    suspend fun getUser(): Response<User>
}