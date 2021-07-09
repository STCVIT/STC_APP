package com.mstc.mstcapp.ui.explore.about;

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
import com.mstc.mstcapp.R;
import com.mstc.mstcapp.adapter.explore.BoardMemberAdapter;
import com.mstc.mstcapp.model.explore.BoardMemberModel;
import com.mstc.mstcapp.util.RetrofitInstance;
import com.mstc.mstcapp.util.RetrofitInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.internal.EverythingIsNonNull;

public class AboutFragment extends Fragment {
    private AboutViewModel mViewModel;
    private BoardMemberAdapter boardMemberAdapter;
    private List<BoardMemberModel> list;
    private SwipeRefreshLayout swipeRefreshLayout;

    public AboutFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.fade));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_swipe_recycler, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        postponeEnterTransition();
        mViewModel = new ViewModelProvider(this).get(AboutViewModel.class);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        Context context = view.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        list = new ArrayList<>();
        boardMemberAdapter = new BoardMemberAdapter(context, list);
        recyclerView.setAdapter(boardMemberAdapter);
        swipeRefreshLayout.setOnRefreshListener(() -> loadData(view));
        mViewModel.getList().observe(getViewLifecycleOwner(), members -> {
            if (members.size() == 0) view.findViewById(R.id.loading).setVisibility(View.VISIBLE);
            else view.findViewById(R.id.loading).setVisibility(View.GONE);
            list = members;
            boardMemberAdapter.setList(list);
            startPostponedEnterTransition();
        });
    }

    private void loadData(View view) {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
        Call<List<BoardMemberModel>> call = retrofitInterface.getBoard();
        call.enqueue(new Callback<List<BoardMemberModel>>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<List<BoardMemberModel>> call, Response<List<BoardMemberModel>> response) {
                if (response.isSuccessful()) {
                    swipeRefreshLayout.setRefreshing(false);
                    List<BoardMemberModel> list = response.body();
                    if (list != null)
                        mViewModel.insertBoardMembers(list);
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    Snackbar.make(view, "Unable to fetch details", BaseTransientBottomBar.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<List<BoardMemberModel>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Snackbar.make(view, "Please check your internet connection…", BaseTransientBottomBar.LENGTH_SHORT)
                        .show();
            }
        });
    }


}