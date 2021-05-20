package com.mstc.mstcapp.ui.explore.event;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mstc.mstcapp.Repository;
import com.mstc.mstcapp.model.explore.EventModel;

import java.util.List;

public class EventViewModel extends AndroidViewModel {
    private Repository repository;
    private LiveData<List<EventModel>> list;

    public EventViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        list = repository.getEvents();
        repository = new Repository(application);
    }

    public LiveData<List<EventModel>> getList() {
        return list;
    }

    public void insertEvents(List<EventModel> list) {
        repository.insertEvents(list);
    }
}
