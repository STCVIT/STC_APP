package `in`.stcvit.stcapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import `in`.stcvit.stcapp.data.feed.FeedInjection
import `in`.stcvit.stcapp.databinding.FragmentSwipeRecyclerBinding
import `in`.stcvit.stcapp.ui.loadState.LoadStateAdapter
import androidx.paging.PagingData
import asRemotePresentationState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FeedFragment : Fragment() {

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
        val viewModel = ViewModelProvider(
            this, FeedInjection.provideViewModelFactory(
                context = requireContext(),
                owner = this
            )
        )[FeedViewModel::class.java]

        // bind the state
        binding.bindState(
            uiState = viewModel.state,
            pagingData = viewModel.pagingDataFlow,
            uiActions = viewModel.accept
        )
    }

    private fun FragmentSwipeRecyclerBinding.bindState(
        uiState: StateFlow<UiState>,
        pagingData: Flow<PagingData<UiModel>>,
        uiActions: (UiAction) -> Unit,
    ) {
        val feedAdapter = FeedAdapter()
        val header = LoadStateAdapter { feedAdapter.retry() }

        recyclerView.adapter = feedAdapter.withLoadStateHeaderAndFooter(
            header = header,
            footer = LoadStateAdapter { feedAdapter.retry() }
        )

        bindRecyclerView(
            header = header,
            feedAdapter = feedAdapter,
            uiState = uiState,
            pagingData= pagingData,
            onScrollChanged = uiActions
        )
    }


    private fun FragmentSwipeRecyclerBinding.bindRecyclerView(
        header: LoadStateAdapter,
        feedAdapter: FeedAdapter,
        uiState: StateFlow<UiState>,
        pagingData:Flow<PagingData<UiModel>>,
        onScrollChanged: (UiAction.Scroll) -> Unit,
    ) {
        swipeRefreshLayout.setOnRefreshListener {
            feedAdapter.refresh()
            swipeRefreshLayout.isRefreshing = false
        }
        retryButton.setOnClickListener { feedAdapter.retry() }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy != 0)
                    onScrollChanged(UiAction.Scroll(currentQuery = uiState.value.query))
            }
        })
        val notLoading = feedAdapter.loadStateFlow
            .asRemotePresentationState()
            .map { it == RemotePresentationState.PRESENTED }

        val hasNotScrolledForCurrentSearch = uiState
            .map { it.hasNotScrolledForCurrentSearch }
            .distinctUntilChanged()

        val shouldScrollToTop = combine(
            notLoading,
            hasNotScrolledForCurrentSearch,
            Boolean::and
        ).distinctUntilChanged()

       lifecycleScope.launch {
           pagingData.collectLatest(feedAdapter::submitData)
       }

        lifecycleScope.launch {
            shouldScrollToTop.collect { shouldScroll ->
                if (shouldScroll) recyclerView.scrollToPosition(0)
            }
        }

        lifecycleScope.launch {
            feedAdapter.loadStateFlow.collect { loadState ->
                // Show a retry header if there was an error refreshing, and items were previously
                // cached OR default to the default prepend state
                header.loadState = loadState.mediator
                    ?.refresh
                    ?.takeIf { it is LoadState.Error && feedAdapter.itemCount > 0 }
                    ?: loadState.prepend

                // Only show the list if refresh succeeds, either from the the local db or the remote.
                recyclerView.isVisible =
                    loadState.source.refresh is LoadState.NotLoading || loadState.mediator?.refresh is LoadState.NotLoading
                // Show loading spinner during initial load or refresh.
                swipeRefreshLayout.isRefreshing = loadState.mediator?.refresh is LoadState.Loading
                // Show the retry state if initial load or refresh fails.
                errorLayout.isVisible =
                    loadState.mediator?.refresh is LoadState.Error && feedAdapter.itemCount == 0
            }
        }
    }
}