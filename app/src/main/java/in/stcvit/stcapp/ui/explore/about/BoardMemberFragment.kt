package `in`.stcvit.stcapp.ui.explore.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import `in`.stcvit.stcapp.databinding.FragmentAboutBinding
import `in`.stcvit.stcapp.model.explore.BoardMember
import `in`.stcvit.stcapp.model.Result

class BoardMemberFragment : Fragment() {

    private lateinit var viewModel: BoardMemberViewModel
    private lateinit var binding: FragmentAboutBinding
    private lateinit var boardMemberAdapter: BoardMemberAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[BoardMemberViewModel::class.java]
        boardMemberAdapter = BoardMemberAdapter()
        binding.apply {
            recyclerView.adapter = boardMemberAdapter
            viewModel.getBoard()
                .observe(viewLifecycleOwner) { result ->
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
                }
            retryButton.setOnClickListener { viewModel.refreshBoard() }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.refreshBoard()
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

}