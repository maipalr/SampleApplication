package com.example.sampleapplication.ImageList

import android.icu.text.CaseMap
import com.example.sampleapplication.ImageList.Model.Photo

data class ImageData(
    val photos: Photos
 )

data class Photos(
    val photo: MutableList<Photo>,
    val pages: Int
)
