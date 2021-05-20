package com.mstc.mstcapp.ui.resources.resourceTab;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mstc.mstcapp.Repository;
import com.mstc.mstcapp.model.resources.ResourceModel;

import java.util.List;

public class ResourceTabViewModel extends AndroidViewModel {
    Repository repository;
    LiveData<List<ResourceModel>> listLiveData;

    public ResourceTabViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public LiveData<List<ResourceModel>> getList(String domain) {
        listLiveData = repository.getResources(domain);
        return listLiveData;
    }

    public void insertResources(String domain, List<ResourceModel> list) {
        repository.insertResources(domain, list);
    }
}
