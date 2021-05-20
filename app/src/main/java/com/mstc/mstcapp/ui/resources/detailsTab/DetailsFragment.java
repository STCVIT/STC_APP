package com.mstc.mstcapp.ui.resources.detailsTab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.mstc.mstcapp.R;
import com.mstc.mstcapp.model.resources.DetailModel;
import com.mstc.mstcapp.util.RetrofitInstance;
import com.mstc.mstcapp.util.RetrofitInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class DetailsFragment extends Fragment {
    DetailsViewModel mViewModel;
    private String domain;
    private TextView details, salary;
    private SwipeRefreshLayout swipeRefreshLayout;

    public DetailsFragment() {
    }

    public DetailsFragment(String domain) {
        this.domain = domain;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DetailsViewModel.class);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        details = view.findViewById(R.id.details);
        salary = view.findViewById(R.id.salary);
        mViewModel.getDetails(domain).observe(getViewLifecycleOwner(), detailModel -> {
            if (detailModel == null) {
                view.findViewById(R.id.loading).setVisibility(View.VISIBLE);
                view.findViewById(R.id.linearLayout).setVisibility(View.GONE);
            }
            else {
                view.findViewById(R.id.loading).setVisibility(View.GONE);
                view.findViewById(R.id.linearLayout).setVisibility(View.VISIBLE);
                details.setText(detailModel.getDescription());
                salary.setText(detailModel.getExpectation());
            }
        });
        swipeRefreshLayout.setOnRefreshListener(() -> getData(view));
    }

    private void getData(View view) {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
        Call<DetailModel> call = retrofitInterface.getDetails(domain);
        call.enqueue(new Callback<DetailModel>() {
            @Override
            public void onResponse(@NonNull Call<DetailModel> call, @NonNull Response<DetailModel> response) {
                if (response.isSuccessful()) {
                    swipeRefreshLayout.setRefreshing(false);
                    DetailModel details = response.body();
                    assert details != null;
                    mViewModel.insertDetails(domain, details);
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    Snackbar.make(view, "Unable to fetch details", BaseTransientBottomBar.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<DetailModel> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Snackbar.make(view, "Please check your internet connection…", BaseTransientBottomBar.LENGTH_SHORT)
                        .show();
            }
        });
    }

}