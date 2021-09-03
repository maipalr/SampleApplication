package com.example.sampleapplication.imageList.model

data class ImageData(
    val photos: Photos
 )

data class Photos(
    val photo: MutableList<Photo>,
    val pages: Int
)
