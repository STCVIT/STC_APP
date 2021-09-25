package com.mstc.mstcapp.ui.explore.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mstc.mstcapp.databinding.FragmentAboutBinding
import com.mstc.mstcapp.model.Result
import com.mstc.mstcapp.model.explore.BoardMember

private const val TAG = "AboutFragment"

class AboutFragment : Fragment() {

    companion object {
        fun newInstance() = AboutFragment()
    }

    private lateinit var memberViewModel: BoardMemberViewModel
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
        memberViewModel = ViewModelProvider(this).get(BoardMemberViewModel::class.java)
        boardMemberAdapter = BoardMemberAdapter()
        binding.apply {
            recyclerView.adapter = boardMemberAdapter
            memberViewModel.getBoard()
                .observe(viewLifecycleOwner, { result ->
                    run {
                        when (result) {
                            is Result.Loading -> {
                                errorLayout.visibility = View.GONE
                                swipeRefreshLayout.isRefreshing = true
                                if (result.data != null) {
                                    helper.visibility = View.VISIBLE
                                    boardMemberAdapter.submitList(result.data)
                                }
                            }
                            is Result.Success<List<BoardMember>> -> {
                                boardMemberAdapter.submitList(result.data)
                                errorLayout.visibility = View.GONE
                                swipeRefreshLayout.isRefreshing = false
                                helper.visibility = View.VISIBLE
                            }
                            else -> {
                                helper.visibility = View.GONE
                                errorLayout.visibility = View.VISIBLE
                                swipeRefreshLayout.isRefreshing = false
                            }
                        }
                    }
                })
            retryButton.setOnClickListener { memberViewModel.refreshBoard() }

            swipeRefreshLayout.setOnRefreshListener {
                memberViewModel.refreshBoard()
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

}