package com.mstc.mstcapp.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.transition.TransitionInflater;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.mstc.mstcapp.MainActivity;
import com.mstc.mstcapp.R;
import com.mstc.mstcapp.adapter.FeedAdapter;
import com.mstc.mstcapp.model.FeedModel;
import com.mstc.mstcapp.util.RetrofitInstance;
import com.mstc.mstcapp.util.RetrofitInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.internal.EverythingIsNonNull;

import static com.mstc.mstcapp.util.Functions.isNetworkAvailable;

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    HomeViewModel mViewModel;
    FeedAdapter adapter;
    List<FeedModel> feedList;
    int skip = 1;
    boolean isLoading = false;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Context context;

    public HomeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setExitTransition(inflater.inflateTransition(R.transition.fade));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_swipe_recycler, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        context = view.getContext();
        recyclerView = view.findViewById(R.id.recyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        feedList = new ArrayList<>();
        adapter = new FeedAdapter(context, feedList);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            MainActivity.setFeed_position(0);
            getData(view, 1);
        });
        mViewModel.getList().observe(getViewLifecycleOwner(), list -> {
            if (list.size() == 0) view.findViewById(R.id.loading).setVisibility(View.VISIBLE);
            else view.findViewById(R.id.loading).setVisibility(View.GONE);
            feedList = list;
            adapter.setList(feedList);
            Objects.requireNonNull(recyclerView.getLayoutManager()).scrollToPosition(MainActivity.getFeed_position());
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (linearLayoutManager != null) {
                    int position = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                    if (position != -1)
                        MainActivity.setFeed_position(position);
                    if (!isLoading) {
                        if (linearLayoutManager.findLastVisibleItemPosition() == feedList.size() - 1) {
                            loadMore(view, ++skip);
                            isLoading = true;
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        MainActivity.isHome = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.isHome = true;
    }

    private void loadMore(View view, int skip) {
        if (isNetworkAvailable(context)) {
            isLoading = true;
            feedList.add(null);
            recyclerView.post(() -> adapter.notifyItemInserted(feedList.size() - 1));
            getData(view, skip);

        } else {
            isLoading = false;
            swipeRefreshLayout.setRefreshing(false);
            Snackbar.make(recyclerView, "Please check your internet connection…", BaseTransientBottomBar.LENGTH_SHORT)
                    .setAction("Try Again", v -> loadMore(view, skip))
                    .show();
        }
    }

    private void getData(View view, int skip) {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
        Call<List<FeedModel>> call = retrofitInterface.getFeed(skip);
        call.enqueue(new Callback<List<FeedModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<FeedModel>> call, @NonNull Response<List<FeedModel>> response) {
                if (response.isSuccessful()) {
                    isLoading = false;
                    swipeRefreshLayout.setRefreshing(false);
                    if (feedList.get(feedList.size() - 1) == null) {
                        feedList.remove(feedList.size() - 1);
                        adapter.notifyItemRemoved(feedList.size());
                    }
                    List<FeedModel> list = response.body();
                    if (list != null)
                        mViewModel.insertFeeds(list);
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<List<FeedModel>> call, Throwable t) {
                isLoading = false;
                if (feedList.get(feedList.size() - 1) == null) {
                    feedList.remove(feedList.size() - 1);
                    adapter.notifyItemRemoved(feedList.size());
                }
                swipeRefreshLayout.setRefreshing(false);
                Snackbar.make(view, "Please check your internet connection…", BaseTransientBottomBar.LENGTH_SHORT)
                        .setAction("Try Again", v -> loadMore(view, skip))
                        .show();
            }
        });
    }
}