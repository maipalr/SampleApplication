package com.example.sampleapplication.imageList.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.sampleapplication.imageList.db.PhotoDatabase
import com.example.sampleapplication.imageList.repository.PhtosRepository
import com.example.sampleapplication.R
import com.example.sampleapplication.databinding.ActivityImageListingBinding


class ImageListingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageListingBinding
    lateinit var viewModel: PhotosViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModelRepository()
        binding = ActivityImageListingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.imgNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
        //binding.bottomNavigationView.setupWithNavController(binding.imgNavHostFragment.findNavController())
    }

    private fun setupViewModelRepository(){
        val db = PhotoDatabase(this)
        val  phtosRepository  = PhtosRepository(db)
        val viewModelProviderFactory = PhotosViewModelProviderFactory(phtosRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(PhotosViewModel::class.java)
    }

}