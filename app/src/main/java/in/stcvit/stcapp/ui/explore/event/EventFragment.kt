package `in`.stcvit.stcapp.ui.explore.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import `in`.stcvit.stcapp.databinding.FragmentSwipeRecyclerBinding
import `in`.stcvit.stcapp.model.explore.Event
import `in`.stcvit.stcapp.model.Result

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[EventViewModel::class.java]
        eventAdapter = EventAdapter()
        binding.apply {
            recyclerView.adapter = eventAdapter
            viewModel.getEvents()
                .observe(viewLifecycleOwner) { result ->
                    run {
                        when (result) {
                            is Result.Loading -> {
                                errorLayout.visibility = View.GONE
                                swipeRefreshLayout.isRefreshing = true
                                result.data?.let {
                                    eventAdapter.submitList(it)
                                }
                            }
                            is Result.Success<List<Event>> -> {
                                eventAdapter.submitList(result.data)
                                errorLayout.visibility = View.GONE
                                swipeRefreshLayout.isRefreshing = false
                            }
                            else -> {
                                errorLayout.visibility = View.VISIBLE
                                swipeRefreshLayout.isRefreshing = false
                            }
                        }
                    }
                }
            retryButton.setOnClickListener { viewModel.refreshEvents() }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.refreshEvents()
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }
}
