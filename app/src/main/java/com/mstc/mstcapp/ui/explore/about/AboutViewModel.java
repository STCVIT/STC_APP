package com.mstc.mstcapp.ui.explore.about;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mstc.mstcapp.Repository;
import com.mstc.mstcapp.model.explore.BoardMemberModel;

import java.util.List;

public class AboutViewModel extends AndroidViewModel {
    Repository repository;
    LiveData<List<BoardMemberModel>> list;

    public AboutViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        list = repository.getBoardMembers();
    }

    public LiveData<List<BoardMemberModel>> getList() {
        return list;
    }

    public void insertBoardMembers(List<BoardMemberModel> list) {
        repository.insertBoardMembers(list);
    }
}
