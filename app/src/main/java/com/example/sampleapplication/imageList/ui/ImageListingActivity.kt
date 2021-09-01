package com.example.sampleapplication.imageList.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sampleapplication.imageList.adapter.ImageAdapter
import com.example.sampleapplication.imageList.util.Constants.Companion.QUERY_PAGE_SIZE
import com.example.sampleapplication.imageList.util.Constants.Companion.SEARCH_DELAY_INTERVAL
import com.example.sampleapplication.imageList.util.Resource
import com.example.sampleapplication.imageList.db.PhotoDatabase
import com.example.sampleapplication.imageList.repository.PhtosRepository
import com.example.sampleapplication.R
import com.example.sampleapplication.databinding.ActivityImageListingBinding
import com.example.sampleapplication.profile.Person
import com.example.sampleapplication.profile.ViewPagerActivity
import kotlinx.coroutines.*


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
        setupViewModelRepository()

        var launchSomeActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                // your operation...
            }
        }
        imgAdapter.setOnItemClickListener { photo ->
            Intent(this, PhotoPreviewActivity::class.java).also {
                it.putExtra("Photo", photo)
                //startActivity(it)
                launchSomeActivity.launch(it)
            }
        }
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

    private fun setupSearchView(searchView: SearchView){
        var job: Job? = null
       searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                callSearch(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                callSearch(query)
                return false
            }

            fun callSearch(query: String) {
                job?.cancel()
                job = MainScope().launch {
                    delay(SEARCH_DELAY_INTERVAL)
                    if (query.isNotEmpty()){
                        binding.rcView.setPadding(0,0,0,50)
                        isLastPage = false
                        viewModel.getImages(query)
                    }
                }
            }
        })
    }

    private fun setupViewModelRepository(){
        val db = PhotoDatabase(this)
        val  phtosRepository  = PhtosRepository(db)
        val viewModelProviderFactory = PhotosViewModelProviderFactory(phtosRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(PhotosViewModel::class.java)
        viewModel.images.observe(this, Observer { response ->
            when (response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {
                        imgAdapter.differ.submitList(it.photos.photo.toList())
                        //TODO: remove hardcoded 3
                        isLastPage = viewModel.pageNumber > 3//(viewModel.imagesResponse?.photos?.pages ?: 0)
                        if (isLastPage){
                            binding.rcView.setPadding(0,0,0,0)
                            binding.rcView.requestLayout()
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
                viewModel.getMoreImages()
                isScrolling = false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val search = menu?.findItem(R.id.searchView)
        val searchView = search?.actionView as? SearchView
        searchView?.let {
            setupSearchView(it)
        }
        return true
    }
}