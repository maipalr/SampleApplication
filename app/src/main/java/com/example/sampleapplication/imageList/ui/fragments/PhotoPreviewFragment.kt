package com.example.sampleapplication.imageList.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.sampleapplication.databinding.FragmentPhotoPreviewBinding
import com.example.sampleapplication.imageList.model.Photo
import com.example.sampleapplication.imageList.ui.ImageListingActivity
import com.example.sampleapplication.imageList.ui.PhotosViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [PhotoPreviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PhotoPreviewFragment : Fragment() {
    lateinit var photo: Photo
    lateinit var viewModel: PhotosViewModel
    val args: PhotoPreviewFragmentArgs by navArgs()
    private lateinit var binding: FragmentPhotoPreviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPhotoPreviewBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as ImageListingActivity).viewModel
        photo = args.photo
        val url = "https://farm" + photo.farm + ".staticflickr.com/" + photo.server + "/" + photo.id + "_" + photo.secret+"_c.jpg"
        Glide.with(this)
            .load(url)
            .centerCrop()
            .into(binding.imageView2)
        binding.fab.setOnClickListener{
            viewModel.savePhoto(photo)
            Toast.makeText(activity, "Photo saved!", Toast.LENGTH_SHORT).show()
        }
    }

}