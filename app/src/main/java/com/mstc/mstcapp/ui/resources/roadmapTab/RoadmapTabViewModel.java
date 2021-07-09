package com.mstc.mstcapp.ui.resources.roadmapTab;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mstc.mstcapp.Repository;
import com.mstc.mstcapp.model.resources.RoadmapModel;

public class RoadmapTabViewModel extends AndroidViewModel {
    Repository repository;
    LiveData<RoadmapModel> roadmap;

    public RoadmapTabViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public LiveData<RoadmapModel> getRoadmap(String domain) {
        roadmap = repository.getRoadmap(domain);
        return roadmap;
    }

    public void insertRoadmap(String domain, RoadmapModel roadmapModel) {
        repository.insertRoadmap(domain, roadmapModel);
    }
}
