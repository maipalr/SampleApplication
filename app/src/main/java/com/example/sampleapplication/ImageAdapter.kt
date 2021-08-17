package com.example.sampleapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ImageAdapter(
    var imageList: List<Item>
) : RecyclerView.Adapter<ImageAdapter.ImageItemHolder>() {
    inner class ImageItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent,false)
        return  ImageItemHolder(view)
    }

    override fun onBindViewHolder(holder: ImageItemHolder, position: Int) {
          holder.itemView.apply {
              val textView = findViewById<TextView>(R.id.textView2)
              textView.text = imageList[position].title
        }
    }

    override fun getItemCount(): Int {
       return imageList.size
    }
}