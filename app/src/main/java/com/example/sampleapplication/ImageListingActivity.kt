package com.example.sampleapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sampleapplication.databinding.ActivityImageListingBinding
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException


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

        lifecycleScope.launchWhenCreated {
            val response = try {
                val input = HashMap<String, String>()
                input["method"] = "flickr.photos.getRecent" //"flickr.photos.search"
                input["api_key"] = "062a6c0c49e4de1d78497d13a7dbb360"
               // input["tags"] = "car"
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
                Log.w("TAG", Gson().toJson(response))
            }else{
                print("fail")
                print(response.code().toString())
                Log.i("TAG", response.code().toString())
            }

        }
    }
}