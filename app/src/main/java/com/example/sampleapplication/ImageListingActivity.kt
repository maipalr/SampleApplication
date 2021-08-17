package com.example.sampleapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.ims.ImsMmTelManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sampleapplication.databinding.ActivityImageListingBinding

class ImageListingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageListingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageListingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var itemList = mutableListOf(
            Item( "First", ""),
            Item("Second", "")
        )
        val adapter = ImageAdapter(imageList =itemList)
        binding.rcView.adapter = adapter
        binding.rcView.layoutManager = LinearLayoutManager(this)
    }
}