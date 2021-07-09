package com.mstc.mstcapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.mstc.mstcapp.model.FeedModel;
import com.mstc.mstcapp.model.explore.BoardMemberModel;
import com.mstc.mstcapp.model.explore.EventModel;
import com.mstc.mstcapp.model.explore.ProjectsModel;
import com.mstc.mstcapp.model.resources.DetailModel;
import com.mstc.mstcapp.model.resources.ResourceModel;
import com.mstc.mstcapp.model.resources.RoadmapModel;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface DatabaseDao {

    /**
     *  This database dao is used to perform specific data operations on the database
     *  without exposing the details of the database.
     *  Database : {@link com.mstc.mstcapp.database.STCDatabase}
     *
     * FEED
     * {@link #insertFeeds(List)}  : To insert multiple feeds
     * {@link #getFeedList()}  : To get all Feeds stored in the database (Will have only 5 feeds stored)
     * {@link #deleteFeed()}  : To delete all feed stored in the database
     *
     * <p> EVENTS
     * {@link #insertEvents(List)}  : Insert List of events
     * {@link #getEventsList()}  : Get list of events (Will have only 5 feeds stored)
     * {@link #deleteEvents()}  : Delete all events
     *
     * <p>PROJECTS
     * {@link #insertProjects(List)} : Insert List of projects
     * {@link #getProjects()}  : Get list of all projects
     * {@link #deleteProjects()}  : Delete all projects
     *
     * <p>BOARD MEMBERS
     * {@link #getBoardMembers()} : Insert List of board members
     * {@link #getBoardMembers()} : Get list of all board members
     * {@link #deleteBoard()} : Delete existing board members
     *
     * <p>DETAILS
     * {@link #insertDetails(DetailModel)} : Insert the details of a domain in the database
     * {@link #getDetails(String)} : Get the details of a domain
     * {@link #deleteDetails(String)} : Delete all resources of a domain
     *
     * <p>ROADMAPS
     * {@link #insertRoadmap(RoadmapModel)} : Insert roadmap
     * {@link #getRoadmap(String)} : Get roadmap from database
     * {@link #deleteRoadmap(String)} : Delete roadmap of a domain
     *
     * <p>RESOURCES
     * {@link #insertResources(List)} : Insert List of resources
     * {@link #getResources(String)} : Get list of resources of a domain
     * {@link #deleteResources(String)} : Delete all resources of a domain
     **/

    /**
     * FEED
     **/
    @Insert(onConflict = REPLACE)
    void insertFeeds(List<FeedModel> list);

    @Query(value = "SELECT * FROM FEED ORDER BY id DESC")
    LiveData<List<FeedModel>> getFeedList();

    @Query(value = "DELETE FROM FEED")
    void deleteFeed();

    /**
     * EVENTS
     **/
    @Insert(onConflict = REPLACE)
    void insertEvents(List<EventModel> eventModels);

    @Query(value = "SELECT * FROM EVENTS ORDER BY id DESC")
    LiveData<List<EventModel>> getEventsList();

    /**
     * PROJECTS
     **/
    @Insert(entity = ProjectsModel.class, onConflict = REPLACE)
    void insertProjects(List<ProjectsModel> projectsModels);

    @Query(value = "SELECT * FROM PROJECTS ORDER BY id DESC")
    LiveData<List<ProjectsModel>> getProjects();


    /**
     * BOARD MEMBER
     **/
    @Insert(onConflict = REPLACE)
    void insertBoard(List<BoardMemberModel> boardMembers);

    @Query(value = "SELECT * FROM BOARD")
    LiveData<List<BoardMemberModel>> getBoardMembers();

    @Query("DELETE FROM BOARD")
    void deleteBoard();

    /**
     * DETAILS
     **/
    @Insert(onConflict = REPLACE)
    void insertDetails(DetailModel details);

    @Query("SELECT * FROM DETAILS WHERE domain = :domain")
    LiveData<DetailModel> getDetails(String domain);

    @Query("DELETE FROM DETAILS WHERE domain = :domain")
    void deleteDetails(String domain);

    /**
     * ROADMAP
     **/
    @Insert(onConflict = REPLACE)
    void insertRoadmap(RoadmapModel roadmap);

    @Query("SELECT * FROM ROADMAPS WHERE domain=:domain")
    LiveData<RoadmapModel> getRoadmap(String domain);

    @Query("DELETE FROM ROADMAPS WHERE domain=:domain")
    void deleteRoadmap(String domain);

    /**
     * RESOURCES
     **/
    @Insert(onConflict = REPLACE)
    void insertResources(List<ResourceModel> list);


    @Query("SELECT * FROM RESOURCES WHERE domain=:domain")
    LiveData<List<ResourceModel>> getResources(String domain);

    @Query("DELETE FROM RESOURCES WHERE domain=:domain")
    void deleteResources(String domain);


}
