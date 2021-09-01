package com.example.sampleapplication.imageList.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sampleapplication.imageList.model.ImageData
import com.example.sampleapplication.imageList.util.Resource
import com.example.sampleapplication.imageList.repository.PhtosRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class PhotosViewModel(
    val phtosRepository: PhtosRepository
): ViewModel() {
    val images: MutableLiveData<Resource<ImageData>> = MutableLiveData()
    var pageNumber: Int = 1
    var searchQuery: String = ""
    var imagesResponse: ImageData? = null

    init {
        getImages("")
    }

    fun getImages(searchQuery: String) = viewModelScope.launch {
        pageNumber = 1
        imagesResponse = null
        this@PhotosViewModel.searchQuery = searchQuery
        images.postValue(null)
        getMoreImages()
    }

    fun getMoreImages() = viewModelScope.launch {
        images.postValue(Resource.Loading())
        val response = phtosRepository.getImages(searchQuery,pageNumber)
        images.postValue(handleImagesResponse(response))
    }

    private fun handleImagesResponse(response: Response<ImageData>) : Resource<ImageData>{
        if (response.isSuccessful){
            response.body()?.let {
                pageNumber++
                if (imagesResponse == null) {
                    imagesResponse = it
                }else{
                    val  oldImages = imagesResponse?.photos?.photo
                    val newImages = it.photos.photo
                    oldImages?.addAll(newImages)
                }
                return Resource.Success(imagesResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }
}