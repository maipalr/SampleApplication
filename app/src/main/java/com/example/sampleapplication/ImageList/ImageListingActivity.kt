package com.example.sampleapplication.ImageList

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Orientation
import com.example.sampleapplication.ImageList.Network.RetrofitInstance
import com.example.sampleapplication.R
import com.example.sampleapplication.databinding.ActivityImageListingBinding
import retrofit2.HttpException
import java.io.IOException


class ImageListingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageListingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageListingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rcView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        val photos : List<Photo> = listOf()
        val adapter = ImageAdapter(imageList = photos)
        binding.rcView.adapter = adapter
        binding.rcView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launchWhenCreated {
            val response = try {
                val input = HashMap<String, String>()
                input["method"] = "flickr.photos.getRecent" //"flickr.photos.search"
                input["api_key"] = "062a6c0c49e4de1d78497d13a7dbb360"
                input["tags"] = "car"
                input["format"] = "json"
                input["nojsoncallback"] = "1"
                input["per_page"] = "10"
                input["page"] = "1"
                RetrofitInstance.api.getImageList(input)
            }catch (e: IOException){
                return@launchWhenCreated
            }catch (e: HttpException){
                return@launchWhenCreated
            }
            if (response.isSuccessful){
                print("success")
                print(response.code().toString())
                Log.i("TAG", response.code().toString())
                adapter.imageList = response.body()?.photos?.photo ?: photos
            }else{
                print("fail")
                print(response.code().toString())
                Log.i("TAG", response.code().toString())
                adapter.imageList = photos
            }
            //update your UI
            adapter.notifyDataSetChanged()
        }
    }
}