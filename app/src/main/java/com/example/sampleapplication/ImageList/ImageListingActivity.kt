package com.example.sampleapplication.ImageList

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sampleapplication.ImageList.Util.Constants.Companion.QUERY_PAGE_SIZE
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
    var searchQuery: String? = null

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
        isLoading = false
    }

    private fun showProgressBar(){
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun setupRecyclerView(){
        binding.rcView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        imgAdapter = ImageAdapter()
        binding.rcView.adapter = imgAdapter
        binding.rcView.layoutManager = LinearLayoutManager(this)
        binding.rcView.addOnScrollListener(this@ImageListingActivity.scrollListener)
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
                        searchQuery = query
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
                        imgAdapter.differ.submitList(it.photos.photo.toList())
                        isLastPage = viewModel.pageNumber == (viewModel.imagesResponse?.photos?.pages ?: 0)
                        if (isLastPage){
                            binding.rcView.setPadding(0,0,0,0)
                        }
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

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object  : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = (firstVisibleItemPosition+visibleItemCount) >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling
            if (shouldPaginate){
                viewModel.getImages(searchQuery ?: "")
                isScrolling = false
            }
        }
    }
}