package com.example.sampleapplication.imageList.repository

import com.example.sampleapplication.imageList.model.ImageData
import com.example.sampleapplication.imageList.network.RetrofitInstance
import com.example.sampleapplication.imageList.util.Constants.Companion.API_KEY
import com.example.sampleapplication.imageList.util.Constants.Companion.BASE_URL
import com.example.sampleapplication.imageList.util.Constants.Companion.QUERY_PAGE_SIZE
import com.example.sampleapplication.imageList.db.PhotoDatabase
import retrofit2.Response

class PhtosRepository(
    val db: PhotoDatabase
    ){
    suspend fun getImages(searchQuery: String, pageNumber: Int): Response<ImageData> {
        val input = HashMap<String, String>()
        input["method"] = BASE_URL
        input["api_key"] = API_KEY
        input["format"] = "json"
        input["nojsoncallback"] = "1"
        input["per_page"] = QUERY_PAGE_SIZE.toString()
        input["tags"] = searchQuery
        input["page"] =  pageNumber.toString()
        return RetrofitInstance.api.getImageList(input)
    }
}