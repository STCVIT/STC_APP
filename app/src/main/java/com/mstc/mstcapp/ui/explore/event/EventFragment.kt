package com.mstc.mstcapp.ui.explore.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mstc.mstcapp.databinding.FragmentSwipeRecyclerBinding
import com.mstc.mstcapp.model.Result
import com.mstc.mstcapp.model.explore.Event

private const val TAG = "EventFragment"

class EventFragment : Fragment() {
    private lateinit var viewModel: EventViewModel
    private lateinit var eventAdapter: EventAdapter
    private lateinit var binding: FragmentSwipeRecyclerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSwipeRecyclerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EventViewModel::class.java)
        eventAdapter = EventAdapter()
        binding.apply {
            recyclerView.adapter = eventAdapter
            viewModel.getEvents()
                .observe(viewLifecycleOwner, { result ->
                    run {
                        when (result) {
                            is Result.Loading -> {
                                retryButton.visibility = View.GONE
                                swipeRefreshLayout.isRefreshing = true
                                result.data?.let {
                                    eventAdapter.list = it
                                }
                            }
                            is Result.Success<List<Event>> -> {
                                eventAdapter.list = result.data
                                retryButton.visibility = View.GONE
                                swipeRefreshLayout.isRefreshing = false
                            }
                            else -> {
                                retryButton.visibility = View.VISIBLE
                                swipeRefreshLayout.isRefreshing = false
                            }
                        }
                    }
                })
            retryButton.setOnClickListener { viewModel.refreshEvents() }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.refreshEvents()
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }
}
