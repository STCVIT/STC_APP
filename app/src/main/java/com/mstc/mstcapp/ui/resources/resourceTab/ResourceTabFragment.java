package com.mstc.mstcapp.ui.resources.resourceTab;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.mstc.mstcapp.R;
import com.mstc.mstcapp.adapter.resource.ResourceTabAdapter;
import com.mstc.mstcapp.model.resources.ResourceModel;
import com.mstc.mstcapp.util.RetrofitInstance;
import com.mstc.mstcapp.util.RetrofitInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.internal.EverythingIsNonNull;

public class ResourceTabFragment extends Fragment {
    private RecyclerView recyclerView;
    private ResourceTabViewModel mViewModel;
    private ResourceTabAdapter adapter;
    private List<ResourceModel> list;
    private String domain;
    private SwipeRefreshLayout swipeRefreshLayout;

    public ResourceTabFragment() {
    }

    public ResourceTabFragment(String domain) {
        this.domain = domain;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_swipe_recycler, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ResourceTabViewModel.class);
        Context context = view.getContext();
        recyclerView = view.findViewById(R.id.recyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        list = new ArrayList<>();
        adapter = new ResourceTabAdapter(context, list);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        mViewModel.getList(domain).observe(getViewLifecycleOwner(), eventObjects -> {
            if (eventObjects.size() == 0)
                view.findViewById(R.id.loading).setVisibility(View.VISIBLE);
            else view.findViewById(R.id.loading).setVisibility(View.GONE);
            list = eventObjects;
            adapter.setList(list);
        });
        swipeRefreshLayout.setOnRefreshListener(() -> getData(view));
    }


    private void getData(View view) {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
        Call<List<ResourceModel>> call = retrofitInterface.getResources(domain);
        call.enqueue(new Callback<List<ResourceModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<ResourceModel>> call, @NonNull Response<List<ResourceModel>> response) {
                if (response.isSuccessful()) {
                    swipeRefreshLayout.setRefreshing(false);
                    List<ResourceModel> list = response.body();
                    if (list != null)
                        mViewModel.insertResources(domain, list);
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    Snackbar.make(view, "Unable to fetch resources", BaseTransientBottomBar.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<List<ResourceModel>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Snackbar.make(view, "Please check your internet connection…", BaseTransientBottomBar.LENGTH_SHORT)
                        .show();
            }
        });
    }
}