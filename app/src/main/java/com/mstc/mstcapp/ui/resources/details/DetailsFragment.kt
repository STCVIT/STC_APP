package com.mstc.mstcapp.ui.resources.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mstc.mstcapp.databinding.FragmentDetailsBinding
import com.mstc.mstcapp.model.Result
import com.mstc.mstcapp.model.resource.Detail

private const val TAG = "DetailsFragment"

class DetailsFragment(val domain: String) : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var viewModel: DetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)
        binding.apply {
            viewModel.getDetails(domain).observe(viewLifecycleOwner, { result ->
                run {
                    when (result) {
                        is Result.Loading -> {
                            Log.i(TAG, "onActivityCreated: $result")
                            swipeRefreshLayout.isRefreshing = true
                            errorLayout.visibility = View.GONE
                            result.data?.let {
                                description.apply {
                                    text = it.description
                                    visibility = View.VISIBLE
                                }
                                cardView.visibility = View.VISIBLE
                                salary.text = it.expectation
                            }
                        }
                        is Result.Success<Detail> -> {
                            swipeRefreshLayout.isRefreshing = false
                            errorLayout.visibility = View.GONE
                            description.apply {
                                text = result.data.description
                                visibility = View.VISIBLE
                            }
                            salary.text = result.data.expectation
                            cardView.visibility = View.VISIBLE
                        }
                        else -> {
                            Log.e(TAG, "onActivityCreated: $result")
                            errorLayout.visibility = View.VISIBLE
                            swipeRefreshLayout.isRefreshing = false
                            cardView.visibility = View.GONE
                            description.visibility = View.GONE
                        }
                    }
                }
            })
            swipeRefreshLayout.setOnRefreshListener {
                viewModel.refreshDetails(domain)
                swipeRefreshLayout.isRefreshing = false
            }
            retryButton.setOnClickListener { viewModel.refreshDetails(domain) }
        }
    }
}
