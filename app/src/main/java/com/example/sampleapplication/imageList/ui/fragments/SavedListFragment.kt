package com.example.sampleapplication.imageList.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sampleapplication.R
import com.example.sampleapplication.databinding.FragmentSavedListBinding
import com.example.sampleapplication.databinding.FragmentSearchListBinding
import com.example.sampleapplication.imageList.adapter.ImageAdapter
import com.example.sampleapplication.imageList.ui.ImageListingActivity
import com.example.sampleapplication.imageList.ui.PhotosViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [SavedListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SavedListFragment : Fragment() {
    private lateinit var binding: FragmentSavedListBinding
    lateinit var imgAdapter: ImageAdapter
    lateinit var viewModel: PhotosViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSavedListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as ImageListingActivity).viewModel
        setupRecyclerView()

        imgAdapter.setOnItemClickListener { photo ->
            val bundle = Bundle().apply {
                putSerializable("photo", photo)
            }
            findNavController().navigate(R.id.action_savedListFragment_to_photoPreviewFragment, bundle)
        }

        viewModel.getSavedPhotos().observe(viewLifecycleOwner, Observer {
            imgAdapter.differ.submitList(it)
        })
    }

    private fun setupRecyclerView(){
        binding.rcView.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
        imgAdapter = ImageAdapter()
        binding.rcView.adapter = imgAdapter
        binding.rcView.layoutManager = LinearLayoutManager(activity)
    }

}