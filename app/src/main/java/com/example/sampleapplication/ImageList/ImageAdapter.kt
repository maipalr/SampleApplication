package com.example.sampleapplication.ImageList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sampleapplication.R
import com.example.sampleapplication.R.drawable

class ImageAdapter(
    var imageList: List<Photo>
) : RecyclerView.Adapter<ImageAdapter.ImageItemHolder>() {
    inner class ImageItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent,false)
        return  ImageItemHolder(view)
    }

    override fun onBindViewHolder(holder: ImageItemHolder, position: Int) {
          holder.itemView.apply {
              val metaData = imageList[position]
              val textView = findViewById<TextView>(R.id.textView2)
              textView.text = metaData.title

              val imgView = findViewById<ImageView>(R.id.imageView)
              val url = "https://live.staticflickr.com/" + metaData.server + "/" + metaData.id + "_" + metaData.secret+"_m.jpg"
              Glide.with(this)
                  .load(url)
                  .centerCrop()
                  .into(imgView)
        }
    }

    override fun getItemCount(): Int {
       return imageList.size
    }
}