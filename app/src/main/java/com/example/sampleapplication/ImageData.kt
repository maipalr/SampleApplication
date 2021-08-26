package com.example.sampleapplication

data class ImageData(
    val photos: Photos
 )

data class Photos(
    val photo: List<Photo>
)

data class Photo(
    val farm: String,
    val server: String,
    val id: String,
    val secret: String
)