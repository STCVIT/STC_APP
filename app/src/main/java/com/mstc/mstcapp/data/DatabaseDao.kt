package com.mstc.mstcapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mstc.mstcapp.model.explore.BoardMember
import com.mstc.mstcapp.model.explore.Event
import com.mstc.mstcapp.model.explore.Project
import com.mstc.mstcapp.model.resource.Detail
import com.mstc.mstcapp.model.resource.Resource
import com.mstc.mstcapp.model.resource.Roadmap

@Dao
interface DatabaseDao {

    /**
     * RESOURCES
     */

    /**
     * DETAILS
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetails(details: Detail)

    @Query("SELECT * FROM DETAILS WHERE domain = :domain")
    fun getDetails(domain: String): LiveData<Detail?>

    @Query("DELETE FROM DETAILS WHERE domain = :domain")
    suspend fun deleteDetails(domain: String)

    /**
     * ROADMAP
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoadmap(roadmap: Roadmap)

    @Query("SELECT * FROM ROADMAPS WHERE domain=:domain")
    fun getRoadmap(domain: String): LiveData<Roadmap?>

    @Query("DELETE FROM ROADMAPS WHERE domain=:domain")
    suspend fun deleteRoadmap(domain: String)

    /**
     * RESOURCES
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResources(list: List<Resource>)


    @Query("SELECT * FROM RESOURCES WHERE domain=:domain")
    fun getResources(domain: String): LiveData<List<Resource>>

    @Query("DELETE FROM RESOURCES WHERE domain=:domain")
    suspend fun deleteResources(domain: String)


    /**
     * BOARD MEMBERS
     */
    @Query("SELECT * FROM BOARD")
    fun getBoardMembers(): LiveData<List<BoardMember>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBoardMembers(members: List<BoardMember>)

    @Query("DELETE FROM BOARD")
    suspend fun clearBoardMembers()

    /**
     * PROJECTS
     */
    @Query("SELECT * FROM PROJECTS ORDER BY id DESC")
    fun getProjects(): LiveData<List<Project>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProjects(projects: List<Project>)

    @Query("DELETE FROM PROJECTS")
    suspend fun clearAllProjects()

    /**
     * EVENTS
     */
    @Query("SELECT * FROM EVENTS ORDER BY id DESC")
    fun getEvents(): LiveData<List<Event>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<Event>)

    @Query("DELETE FROM EVENTS")
    suspend fun clearAllEvents()

}