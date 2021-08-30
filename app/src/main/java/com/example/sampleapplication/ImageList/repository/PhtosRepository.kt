package com.example.sampleapplication.ImageList.repository

import com.example.sampleapplication.ImageList.ImageData
import com.example.sampleapplication.ImageList.Network.RetrofitInstance
import retrofit2.Response

class PhtosRepository {
    suspend fun getImages(searchQuery: String, pageNumber: Int): Response<ImageData> {
        val input = HashMap<String, String>()
        input["method"] = "flickr.photos.getRecent" //"flickr.photos.search"
        input["api_key"] = "062a6c0c49e4de1d78497d13a7dbb360"
        input["format"] = "json"
        input["nojsoncallback"] = "1"
        input["per_page"] = "10"
        input["tags"] = searchQuery
        input["page"] =  pageNumber.toString()
        return RetrofitInstance.api.getImageList(input)
    }
}