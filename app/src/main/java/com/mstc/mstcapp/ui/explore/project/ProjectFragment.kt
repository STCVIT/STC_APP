package com.mstc.mstcapp.ui.explore.project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mstc.mstcapp.databinding.FragmentSwipeRecyclerBinding
import com.mstc.mstcapp.model.Result
import com.mstc.mstcapp.model.explore.Project

private const val TAG = "ProjectFragment"

class ProjectFragment : Fragment() {
    private lateinit var viewModel: ProjectViewModel
    private lateinit var projectAdapter: ProjectAdapter
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
        viewModel = ViewModelProvider(this).get(ProjectViewModel::class.java)
        projectAdapter = ProjectAdapter()
        binding.recyclerView.adapter = projectAdapter
        viewModel.getProjects()
            .observe(viewLifecycleOwner, { result ->
                run {
                    binding.apply {
                        when (result) {
                            is Result.Loading -> {
                                retryButton.visibility = View.GONE
                                swipeRefreshLayout.isRefreshing = true
                                result.data?.let {
                                    projectAdapter.list = it
                                }
                            }
                            is Result.Success<List<Project>> -> {
                                projectAdapter.list = result.data
                                retryButton.visibility = View.GONE
                                swipeRefreshLayout.isRefreshing = false
                            }
                            else -> {
                                retryButton.visibility = View.VISIBLE
                                swipeRefreshLayout.isRefreshing = false
                            }
                        }
                    }
                }
            })
        binding.retryButton.setOnClickListener { viewModel.refreshProjects() }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshProjects()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }
}