package com.example.sampleapplication.ImageList

import android.icu.text.CaseMap

data class ImageData(
    val photos: Photos
 )

data class Photos(
    val photo: MutableList<Photo>,
    val pages: Int
)

data class Photo(
    val farm: String,
    val server: String,
    val id: String,
    val secret: String,
    val title: String
)