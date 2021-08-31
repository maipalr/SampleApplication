package com.example.sampleapplication.ImageList.repository

import com.example.sampleapplication.ImageList.ImageData
import com.example.sampleapplication.ImageList.Network.RetrofitInstance
import com.example.sampleapplication.ImageList.Util.Constants.Companion.API_KEY
import com.example.sampleapplication.ImageList.Util.Constants.Companion.BASE_URL
import com.example.sampleapplication.ImageList.Util.Constants.Companion.QUERY_PAGE_SIZE
import retrofit2.Response

class PhtosRepository {
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