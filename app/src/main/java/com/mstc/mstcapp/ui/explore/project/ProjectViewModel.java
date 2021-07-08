package com.mstc.mstcapp.ui.explore.project;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mstc.mstcapp.Repository;
import com.mstc.mstcapp.model.explore.ProjectsModel;

import java.util.List;

public class ProjectViewModel extends AndroidViewModel {
    Repository repository;
    LiveData<List<ProjectsModel>> list;

    public ProjectViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        list = repository.getProjects();
    }

    public LiveData<List<ProjectsModel>> getList() {
        return list;
    }

    public void insertProjects(List<ProjectsModel> list) {
        repository.insertProjects(list);
    }

}
