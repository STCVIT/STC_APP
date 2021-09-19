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
        binding.apply {
            recyclerView.adapter = projectAdapter
            viewModel.getProjects()
                .observe(viewLifecycleOwner, { result ->
                    run {
                        when (result) {
                            is Result.Loading -> {
                                errorLayout.visibility = View.GONE
                                swipeRefreshLayout.isRefreshing = true
                                result.data?.let {
                                    projectAdapter.submitList(it)
                                }
                            }
                            is Result.Success<List<Project>> -> {
                                projectAdapter.submitList(result.data)
                                errorLayout.visibility = View.GONE
                                swipeRefreshLayout.isRefreshing = false
                            }
                            else -> {
                                errorLayout.visibility = View.VISIBLE
                                swipeRefreshLayout.isRefreshing = false
                            }
                        }
                    }
                })
            retryButton.setOnClickListener { viewModel.refreshProjects() }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.refreshProjects()
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }
}