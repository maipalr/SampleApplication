package com.example.sampleapplication.ImageList

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sampleapplication.ImageList.Util.Constants.Companion.SEARCH_DELAY_INTERVAL
import com.example.sampleapplication.ImageList.Util.Resource
import com.example.sampleapplication.ImageList.repository.PhtosRepository
import com.example.sampleapplication.databinding.ActivityImageListingBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ImageListingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageListingBinding
    lateinit var viewModel: PhotosViewModel
    lateinit var imgAdapter: ImageAdapter
    val TAG = "ImageListingActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageListingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        setupSearchView()
        setupViewModelRepository()
    }

    private fun hideProgressBar(){
        binding.paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar(){
        binding.paginationProgressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView(){
        binding.rcView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        imgAdapter = ImageAdapter()
        binding.rcView.adapter = imgAdapter
        binding.rcView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupSearchView(){
        var job: Job? = null
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                callSearch(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                callSearch(query)
                return true
            }

            fun callSearch(query: String) {
                job?.cancel()
                job = MainScope().launch {
                    delay(SEARCH_DELAY_INTERVAL)
                    if (query.isNotEmpty()){
                        viewModel.getImages(query)
                    }
                }
            }
        })
    }

    private fun setupViewModelRepository(){
        val  phtosRepository: PhtosRepository  = PhtosRepository()
        val viewModelProviderFactory = PhotosViewModelProviderFactory(phtosRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(PhotosViewModel::class.java)
        viewModel.images.observe(this, Observer { response ->
            when (response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        imgAdapter.differ.submitList(it.photos.photo)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Log.e(TAG,"An error occured: $it")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }
}