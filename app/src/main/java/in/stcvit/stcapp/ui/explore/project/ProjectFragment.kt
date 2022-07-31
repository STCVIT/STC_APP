package `in`.stcvit.stcapp.ui.explore.project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import `in`.stcvit.stcapp.databinding.FragmentSwipeRecyclerBinding
import `in`.stcvit.stcapp.model.explore.Project
import `in`.stcvit.stcapp.model.Result

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ProjectViewModel::class.java]
        projectAdapter = ProjectAdapter()
        binding.apply {
            recyclerView.adapter = projectAdapter
            viewModel.getProjects()
                .observe(viewLifecycleOwner) { result ->
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
                }
            retryButton.setOnClickListener { viewModel.refreshProjects() }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.refreshProjects()
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }
}