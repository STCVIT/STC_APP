package `in`.stcvit.stcapp.ui.resources.resource

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import `in`.stcvit.stcapp.databinding.FragmentSwipeRecyclerBinding
import `in`.stcvit.stcapp.model.resource.Resource
import `in`.stcvit.stcapp.model.Result

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ResourceViewModel::class.java]
        val resourceAdapter = ResourceAdapter()
        binding.apply {
            recyclerView.adapter = resourceAdapter
            recyclerView.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                )
            )
            viewModel.getResources(domain)
                .observe(viewLifecycleOwner) { result ->
                    run {
                        when (result) {
                            is Result.Loading -> {
                                errorLayout.visibility = View.GONE
                                swipeRefreshLayout.isRefreshing = true
                                result.data?.let {
                                    resourceAdapter.submitList(it)
                                }
                            }
                            is Result.Success<List<Resource>> -> {
                                resourceAdapter.submitList(result.data)
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