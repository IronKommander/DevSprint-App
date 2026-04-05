package com.example.krantikari

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val api1: Api by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.chucknorris.io/") // Ends with /
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
    }

    val api2: Api by lazy {
        Retrofit.Builder()
            .baseUrl("https://randomuser.me/") // Ends with /
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)
    }
}