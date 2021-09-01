package com.example.sampleapplication.imageList.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.sampleapplication.databinding.ActivityPhotoPreviewBinding
import com.example.sampleapplication.imageList.model.Photo

class PhotoPreviewActivity : AppCompatActivity() {
    lateinit var photo: Photo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPhotoPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        photo = intent.getSerializableExtra("Photo") as Photo
        val url = "https://farm" + photo.farm + ".staticflickr.com/" + photo.server + "/" + photo.id + "_" + photo.secret+"_c.jpg"
        Glide.with(this)
            .load(url)
            .centerCrop()
            .into(binding.imageView2)
      }
}