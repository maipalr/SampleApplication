package com.example.sampleapplication.imageList.repository

import com.example.sampleapplication.imageList.model.ImageData
import com.example.sampleapplication.imageList.network.RetrofitInstance
import com.example.sampleapplication.imageList.util.Constants.Companion.API_KEY
import com.example.sampleapplication.imageList.util.Constants.Companion.PHOTOS_SEARCH_METHOD
import com.example.sampleapplication.imageList.util.Constants.Companion.QUERY_PAGE_SIZE
import com.example.sampleapplication.imageList.db.PhotoDatabase
import com.example.sampleapplication.imageList.model.Photo
import retrofit2.Response

class PhtosRepository(
    val db: PhotoDatabase
    ){
    suspend fun getImages(searchQuery: String, pageNumber: Int): Response<ImageData> {
        val input = HashMap<String, String>()
        input["method"] = PHOTOS_SEARCH_METHOD
        input["api_key"] = API_KEY
        input["format"] = "json"
        input["nojsoncallback"] = "1"
        input["per_page"] = QUERY_PAGE_SIZE.toString()
        input["tags"] = searchQuery
        input["page"] =  pageNumber.toString()
        return RetrofitInstance.api.getImageList(input)
    }

    suspend fun upsert(photo: Photo) = db.getPhotoDao().upsert(photo)
    fun getSavedPhotos() = db.getPhotoDao().getAllPhotos()
    suspend fun deletePhoto(photo: Photo) = db.getPhotoDao().deletePhoto(photo)
}