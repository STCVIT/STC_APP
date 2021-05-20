package com.mstc.mstcapp.model.explore;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@Entity(tableName = "PROJECTS")
public class ProjectsModel {

    @SerializedName("title")
    private final String title;
    @SerializedName("contributors")
    private final ArrayList<String> contributorsList;
    @SerializedName("link")
    private final String link;
    @SerializedName("description")
    private final String description;

    @PrimaryKey
    @NonNull
    @SerializedName("_id")
    private String id;

    public ProjectsModel(String title, ArrayList<String> contributorsList, String link, String description) {
        this.title = title;
        this.contributorsList = contributorsList;
        this.link = link;
        this.description = description;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }


    public ArrayList<String> getContributorsList() {
        return contributorsList;
    }

    public String getLink() {
        return link;
    }


    public String getDescription() {
        return description;
    }

}
