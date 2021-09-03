package com.example.sampleapplication.imageList.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.AbsListView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sampleapplication.R
import com.example.sampleapplication.databinding.FragmentSearchListBinding
import com.example.sampleapplication.imageList.adapter.ImageAdapter
import com.example.sampleapplication.imageList.ui.ImageListingActivity
import com.example.sampleapplication.imageList.ui.PhotosViewModel
import com.example.sampleapplication.imageList.util.Constants
import com.example.sampleapplication.imageList.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController


/**
 * A simple [Fragment] subclass.
 * Use the [SearchListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchListFragment : Fragment() {
    private lateinit var binding: FragmentSearchListBinding
    lateinit var imgAdapter: ImageAdapter
    lateinit var viewModel: PhotosViewModel
    val TAG = "SearchListFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as ImageListingActivity).viewModel
        setupRecyclerView()

        imgAdapter.setOnItemClickListener { photo ->
            val bundle = Bundle().apply {
                putSerializable("photo", photo)
            }
            findNavController().navigate(R.id.action_searchListFragment_to_photoPreviewFragment, bundle)
        }

        viewModel.images.observe(viewLifecycleOwner, Observer { response ->
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchListBinding.inflate(inflater,container,false)
        return binding.root
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
        binding.rcView.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
        imgAdapter = ImageAdapter()
        binding.rcView.adapter = imgAdapter
        binding.rcView.layoutManager = LinearLayoutManager(activity)
        binding.rcView.addOnScrollListener(this@SearchListFragment.scrollListener)
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
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling
            if (shouldPaginate){
                viewModel.getMoreImages()
                isScrolling = false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val search = menu?.findItem(R.id.searchView)
        val searchView = search?.actionView as? SearchView
        searchView?.let {
            setupSearchView(it)
        }
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
                    delay(Constants.SEARCH_DELAY_INTERVAL)
                    if (query.isNotEmpty()){
                        binding.rcView.setPadding(0,0,0,50)
                        isLastPage = false
                        viewModel.getImages(query)
                    }
                }
            }
        })
    }
}