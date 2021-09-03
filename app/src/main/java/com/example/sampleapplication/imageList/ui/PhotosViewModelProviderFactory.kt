package com.example.sampleapplication.imageList.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sampleapplication.imageList.repository.PhtosRepository

class PhotosViewModelProviderFactory(
    val photosRepository: PhtosRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PhotosViewModel(photosRepository) as T
    }
}