package com.mstc.mstcapp.ui.resources.detailsTab;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mstc.mstcapp.Repository;
import com.mstc.mstcapp.model.resources.DetailModel;

public class DetailsViewModel extends AndroidViewModel {
    Repository repository;
    LiveData<DetailModel> liveData;

    public DetailsViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public LiveData<DetailModel> getDetails(String domain) {
        liveData = repository.getDetails(domain);
        return liveData;
    }

    public void insertDetails(String domain, DetailModel details) {
        repository.insertDetails(domain, details);
    }
}