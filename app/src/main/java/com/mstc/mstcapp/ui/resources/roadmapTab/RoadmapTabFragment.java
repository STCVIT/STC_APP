package com.mstc.mstcapp.ui.resources.roadmapTab;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.mstc.mstcapp.R;
import com.mstc.mstcapp.model.resources.RoadmapModel;
import com.mstc.mstcapp.util.RetrofitInstance;
import com.mstc.mstcapp.util.RetrofitInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RoadmapTabFragment extends Fragment {

    private String domain;
    private ImageView imageView;
    private RoadmapTabViewModel mViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;

    public RoadmapTabFragment() {
    }

    public RoadmapTabFragment(String domain) {
        this.domain = domain;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_roadmap, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = view.findViewById(R.id.roadmapImage);
        mViewModel = new ViewModelProvider(this).get(RoadmapTabViewModel.class);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> getData(view));
        mViewModel.getRoadmap(domain).observe(getViewLifecycleOwner(), roadmapModel -> {
            if (roadmapModel == null) getData(view);
            else {
                view.findViewById(R.id.loading).setVisibility(View.GONE);
                new Thread(() -> imageView.post(() -> {
                    try {
                        byte[] decodedString = Base64.decode(roadmapModel.getImage(), Base64.DEFAULT);
                        Bitmap picture = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imageView.setImageBitmap(picture);
                    } catch (Exception e) {
                        e.printStackTrace();
                        swipeRefreshLayout.setRefreshing(false);
                        Snackbar.make(view, "Please check your internet connection…", BaseTransientBottomBar.LENGTH_SHORT)
                                .show();

                    }
                })).start();
            }
        });
    }

    private void getData(View view) {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
        Call<RoadmapModel> call = retrofitInterface.getRoadmap(domain);
        call.enqueue(new Callback<RoadmapModel>() {
            @Override
            public void onResponse(@NonNull Call<RoadmapModel> call, @NonNull Response<RoadmapModel> response) {
                if (response.isSuccessful()) {
                    swipeRefreshLayout.setRefreshing(false);
                    RoadmapModel roadmap = response.body();
                    if (roadmap != null) mViewModel.insertRoadmap(domain, roadmap);
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    view.findViewById(R.id.loading).setVisibility(View.GONE);
                    Snackbar.make(view, "Uh oh! We are unable to load the roadmap. Please try again later.\n", BaseTransientBottomBar.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<RoadmapModel> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                view.findViewById(R.id.loading).setVisibility(View.GONE);
                Snackbar.make(view, "Please check your internet connection…", BaseTransientBottomBar.LENGTH_SHORT)
                        .show();
            }
        });
    }
}
