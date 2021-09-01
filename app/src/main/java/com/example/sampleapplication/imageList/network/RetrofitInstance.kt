package com.example.sampleapplication.imageList.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: ImageListApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.flickr.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ImageListApi::class.java)
    }
}