package `in`.stcvit.stcapp.ui.resources.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import `in`.stcvit.stcapp.databinding.FragmentDetailsBinding
import `in`.stcvit.stcapp.model.resource.Detail
import `in`.stcvit.stcapp.model.Result

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[DetailsViewModel::class.java]
        binding.apply {
            viewModel.getDetails(domain).observe(viewLifecycleOwner) { result ->
                run {
                    when (result) {
                        is Result.Loading -> {
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
                            errorLayout.visibility = View.VISIBLE
                            swipeRefreshLayout.isRefreshing = false
                            cardView.visibility = View.GONE
                            description.visibility = View.GONE
                        }
                    }
                }
            }
            swipeRefreshLayout.setOnRefreshListener {
                viewModel.refreshDetails(domain)
                swipeRefreshLayout.isRefreshing = false
            }
            retryButton.setOnClickListener { viewModel.refreshDetails(domain) }
        }
    }
}
