package com.mstc.mstcapp.ui.resources.resource

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mstc.mstcapp.databinding.FragmentSwipeRecyclerBinding
import com.mstc.mstcapp.model.Result
import com.mstc.mstcapp.model.resource.Resource

private const val TAG = "ResourceFragment"

class ResourceFragment(val domain: String) : Fragment() {
    private lateinit var binding: FragmentSwipeRecyclerBinding
    private lateinit var viewModel: ResourceViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSwipeRecyclerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ResourceViewModel::class.java)
        val resourceAdapter = ResourceAdapter()
        binding.apply {
            recyclerView.adapter = resourceAdapter
            viewModel.getResources(domain)
                .observe(viewLifecycleOwner, { result ->
                    run {
                        when (result) {
                            is Result.Loading -> {
                                retryButton.visibility = View.GONE
                                swipeRefreshLayout.isRefreshing = true
                                result.data?.let {
                                    resourceAdapter.list = it
                                }
                            }
                            is Result.Success<List<Resource>> -> {
                                resourceAdapter.list = result.data
                                retryButton.visibility = View.GONE
                                swipeRefreshLayout.isRefreshing = false
                            }
                            else -> {
                                Log.i(TAG, "onActivityCreated: $result")
                                retryButton.visibility = View.VISIBLE
                                swipeRefreshLayout.isRefreshing = false
                            }

                        }
                    }
                })
            retryButton.setOnClickListener {
                viewModel.refreshResources(domain)
            }
            swipeRefreshLayout.setOnRefreshListener {
                viewModel.refreshResources(domain)
                swipeRefreshLayout.isRefreshing = false
            }
        }

    }

}