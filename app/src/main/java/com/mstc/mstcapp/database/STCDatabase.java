package com.mstc.mstcapp.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.mstc.mstcapp.model.FeedModel;
import com.mstc.mstcapp.model.explore.BoardMemberModel;
import com.mstc.mstcapp.model.explore.EventModel;
import com.mstc.mstcapp.model.explore.ProjectsModel;
import com.mstc.mstcapp.model.resources.DetailModel;
import com.mstc.mstcapp.model.resources.ResourceModel;
import com.mstc.mstcapp.model.resources.RoadmapModel;
import com.mstc.mstcapp.util.RetrofitInstance;
import com.mstc.mstcapp.util.RetrofitInterface;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

@Database(
        entities = {
                BoardMemberModel.class,
                DetailModel.class,
                EventModel.class,
                FeedModel.class,
                ResourceModel.class,
                RoadmapModel.class,
                ProjectsModel.class
        },
        version = 1,
        exportSchema = false)
public abstract class STCDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "STCDatabase";
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);
    private static final String TAG = "STCDatabase";
    public static volatile STCDatabase INSTANCE = null;

    /**
     * Called when the app is installed. The home page is populated with some feed.
     * So that he does not have to wait for the feed to load the first time he opens the app
     */

    public static Callback callback = new Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            databaseWriteExecutor.execute(() -> {
                DatabaseDao databaseDao = INSTANCE.databaseDao();
                Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
                RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
                Call<List<FeedModel>> feed = retrofitInterface.getFeed(1);
                feed.enqueue(new retrofit2.Callback<List<FeedModel>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<FeedModel>> call, @NonNull Response<List<FeedModel>> response) {
                        if (response.isSuccessful()) {
                            Log.i(TAG, "onResponse: successfull");
                            Log.d(TAG, "onResponse() returned: " + response.body());
                            List<FeedModel> feeds = response.body();
                            STCDatabase.databaseWriteExecutor.execute(() -> {
                                databaseDao.insertFeeds(feeds);
                            });
                        } else {
                            Log.d(TAG, "onResponse() returned: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<FeedModel>> call, @NonNull Throwable t) {
                        Log.e(TAG, "onFailure: ", t);
                    }
                });
                Call<List<EventModel>> events = retrofitInterface.getEvents(1);
                events.enqueue(new retrofit2.Callback<List<EventModel>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<EventModel>> call, @NonNull Response<List<EventModel>> response) {
                        if (response.isSuccessful()) {
                            Log.i(TAG, "onResponse: successfull");
                            Log.d(TAG, "onResponse() returned: " + response.body());
                            List<EventModel> events = response.body();
                            STCDatabase.databaseWriteExecutor.execute(() -> {
                                databaseDao.insertEvents(events);
                            });
                        } else {
                            Log.d(TAG, "onResponse() returned: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<EventModel>> call, @NonNull Throwable t) {
                        Log.e(TAG, "onFailure: ", t);
                    }
                });

                Call<List<BoardMemberModel>> board = retrofitInterface.getBoard();
                board.enqueue(new retrofit2.Callback<List<BoardMemberModel>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<BoardMemberModel>> call, @NonNull Response<List<BoardMemberModel>> response) {
                        if (response.isSuccessful()) {
                            Log.i(TAG, "onResponse: successfull");
                            Log.d(TAG, "onResponse() returned: " + response.body());
                            List<BoardMemberModel> list = response.body();
                            STCDatabase.databaseWriteExecutor.execute(() -> {
                                databaseDao.insertBoard(list);
                            });
                        } else {
                            Log.d(TAG, "onResponse() returned: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<BoardMemberModel>> call, @NonNull Throwable t) {
                        Log.e(TAG, "onFailure: ", t);
                    }
                });
            });
        }
    };

    public static STCDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (STCDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, STCDatabase.class, DATABASE_NAME)
                            .addCallback(callback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract DatabaseDao databaseDao();
}
