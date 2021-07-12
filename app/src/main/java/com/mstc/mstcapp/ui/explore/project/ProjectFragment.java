package com.mstc.mstcapp.ui.explore.project;

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

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.mstc.mstcapp.R;
import com.mstc.mstcapp.adapter.explore.ProjectsAdapter;
import com.mstc.mstcapp.model.explore.ProjectsModel;
import com.mstc.mstcapp.util.RetrofitInstance;
import com.mstc.mstcapp.util.RetrofitInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.internal.EverythingIsNonNull;

import static com.mstc.mstcapp.util.Functions.isNetworkAvailable;

public class ProjectFragment extends Fragment {
    int skip = 1;
    boolean isLoading = false;
    private RecyclerView recyclerView;
    private ProjectViewModel mViewModel;
    private ProjectsAdapter projectsAdapter;
    private List<ProjectsModel> list;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Context context;

    public ProjectFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_swipe_recycler, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProjectViewModel.class);
        recyclerView = view.findViewById(R.id.recyclerView);
        context = view.getContext();
        recyclerView = view.findViewById(R.id.recyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        list = new ArrayList<>();
        projectsAdapter = new ProjectsAdapter(context, list);
        recyclerView.setAdapter(projectsAdapter);
        swipeRefreshLayout.setOnRefreshListener(() -> getData(view, 1));
        mViewModel.getList().observe(getViewLifecycleOwner(), projectsModels -> {
            if (projectsModels.size() == 0)
                view.findViewById(R.id.loading).setVisibility(View.VISIBLE);
            else view.findViewById(R.id.loading).setVisibility(View.GONE);
            list = projectsModels;
            projectsAdapter.setList(list);
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading) {
                    if (linearLayoutManager != null &&
                            linearLayoutManager.findLastVisibleItemPosition() == list.size() - 1) {
                        loadMore(view, ++skip);
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void loadMore(View view, int skip) {
        if (isNetworkAvailable(context)) {
            isLoading = true;
            list.add(null);
            recyclerView.post(() -> projectsAdapter.notifyItemInserted(list.size() - 1));
            getData(view, skip);
        } else {
            isLoading = false;
            swipeRefreshLayout.setRefreshing(false);
            Snackbar.make(view, "Please check your internet connection…", BaseTransientBottomBar.LENGTH_SHORT)
                    .setAction("Try Again", v -> loadMore(view, skip))
                    .show();
        }
    }

    private void getData(View view, int skip) {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
        Call<List<ProjectsModel>> call = retrofitInterface.getProjects(skip);
        call.enqueue(new Callback<List<ProjectsModel>>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<List<ProjectsModel>> call, Response<List<ProjectsModel>> response) {
                if (response.isSuccessful()) {
                    isLoading = false;
                    swipeRefreshLayout.setRefreshing(false);
                    if (list.get(list.size() - 1) == null) {
                        list.remove(list.size() - 1);
                        projectsAdapter.notifyItemRemoved(list.size());
                    }
                    List<ProjectsModel> list = response.body();
                    if (list != null)
                        mViewModel.insertProjects(list);
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<List<ProjectsModel>> call, Throwable t) {
                isLoading = false;
                if (list.get(list.size() - 1) == null) {
                    list.remove(list.size() - 1);
                    projectsAdapter.notifyItemRemoved(list.size());
                }
                swipeRefreshLayout.setRefreshing(false);
                Snackbar.make(view, "Please check your internet connection…",
                        BaseTransientBottomBar.LENGTH_SHORT)
                        .show();
            }
        });
    }
}