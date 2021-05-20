package com.mstc.mstcapp.ui.home;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mstc.mstcapp.Repository;
import com.mstc.mstcapp.model.FeedModel;
import com.mstc.mstcapp.util.RetrofitInstance;
import com.mstc.mstcapp.util.RetrofitInterface;

import java.util.List;

import retrofit2.Retrofit;

public class HomeViewModel extends AndroidViewModel {
    Repository repository;
    private LiveData<List<FeedModel>> listFeed;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        listFeed = repository.getSavedFeedList();
    }

    public LiveData<List<FeedModel>> getList() {
        listFeed = repository.getSavedFeedList();
        return listFeed;
    }

    public void insertFeeds(List<FeedModel> list) {
        repository.insertFeeds(list);
    }
}