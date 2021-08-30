package com.example.sampleapplication.ImageList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sampleapplication.R
import com.example.sampleapplication.R.drawable

class ImageAdapter() : RecyclerView.Adapter<ImageAdapter.ImageItemHolder>() {
    inner class ImageItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Photo>(){
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
           return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    private var onItemClickListener: ((Photo) -> Unit)?= null

    fun setOnItemClickListener(listener: (Photo) -> Unit){
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent,false)
        return  ImageItemHolder(view)
    }

    override fun onBindViewHolder(holder: ImageItemHolder, position: Int) {
          holder.itemView.apply {
              val metaData = differ.currentList[position]
              val textView = findViewById<TextView>(R.id.textView2)
              textView.text = metaData.title

              val imgView = findViewById<ImageView>(R.id.imageView)
              val url = "https://live.staticflickr.com/" + metaData.server + "/" + metaData.id + "_" + metaData.secret+"_m.jpg"
              Glide.with(this)
                  .load(url)
                  .centerCrop()
                  .into(imgView)

              setOnClickListener {
                  onItemClickListener?.let {
                      it(metaData)
                  }
              }
        }
    }

    override fun getItemCount(): Int {
       return differ.currentList.size
    }
}