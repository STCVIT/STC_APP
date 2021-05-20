package com.mstc.mstcapp.util;

import com.mstc.mstcapp.model.FeedModel;
import com.mstc.mstcapp.model.explore.BoardMemberModel;
import com.mstc.mstcapp.model.explore.EventModel;
import com.mstc.mstcapp.model.explore.ProjectsModel;
import com.mstc.mstcapp.model.resources.DetailModel;
import com.mstc.mstcapp.model.resources.ResourceModel;
import com.mstc.mstcapp.model.resources.RoadmapModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitInterface {

    @GET("project")
    Call<List<ProjectsModel>> getProjects();

    @GET("event")
    Call<List<EventModel>> getEvents(@Query("skip") int skip);

    @GET("feed")
    Call<List<FeedModel>> getFeed(@Query("skip") int skip);

    @GET("details/{domain}")
    Call<DetailModel> getDetails(@Path("domain") String domain);

    @GET("resource/{domain}")
    Call<List<ResourceModel>> getResources(@Path("domain") String domain);

    @GET("roadmap/{domain}")
    Call<RoadmapModel> getRoadmap(@Path("domain") String domain);

    @GET("board")
    Call<List<BoardMemberModel>> getBoard();

}


