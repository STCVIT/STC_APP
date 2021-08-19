package com.mstc.mstcapp.ui.explore.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mstc.mstcapp.databinding.FragmentAboutBinding
import com.mstc.mstcapp.databinding.FragmentSwipeRecyclerBinding
import com.mstc.mstcapp.model.Result
import com.mstc.mstcapp.model.explore.BoardMember

private const val TAG = "AboutFragment"

class AboutFragment : Fragment() {

    companion object {
        fun newInstance() = AboutFragment()
    }

    private lateinit var viewModel: AboutViewModel
    private lateinit var binding: FragmentAboutBinding
    private lateinit var boardMemberAdapter: BoardMemberAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AboutViewModel::class.java)
        boardMemberAdapter = BoardMemberAdapter()
        binding.apply {
            recyclerView.adapter = boardMemberAdapter
            viewModel.getBoard()
                .observe(viewLifecycleOwner, { result ->
                    run {
                        when (result) {
                            is Result.Loading -> {
                                retryButton.visibility = View.GONE
                                swipeRefreshLayout.isRefreshing = true
                                if (result.data != null) {
                                    boardMemberAdapter.list = result.data
                                }
                            }
                            is Result.Success<List<BoardMember>> -> {
                                boardMemberAdapter.list = result.data
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
            retryButton.setOnClickListener { viewModel.refreshBoard() }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.refreshBoard()
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

}