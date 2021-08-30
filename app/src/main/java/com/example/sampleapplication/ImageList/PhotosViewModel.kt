package com.example.sampleapplication.ImageList

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sampleapplication.ImageList.Util.Resource
import com.example.sampleapplication.ImageList.repository.PhtosRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class PhotosViewModel(
    val phtosRepository: PhtosRepository
): ViewModel() {
    val images: MutableLiveData<Resource<ImageData>> = MutableLiveData()
    var pageNumber: Int = 1

    init {
        getImages("")
    }

    fun getImages(searchQuery: String) = viewModelScope.launch {
        images.postValue(Resource.Loading())
        val response = phtosRepository.getImages(searchQuery,pageNumber)
        images.postValue(handleImagesResponse(response))
    }

    private fun handleImagesResponse(response: Response<ImageData>) : Resource<ImageData>{
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }
}