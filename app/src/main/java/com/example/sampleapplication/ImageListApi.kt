package com.example.sampleapplication

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ImageListApi {
    @GET("/services/rest/")
    suspend fun getImageList(@QueryMap input: HashMap<String, String>): Response<ImageData>
}