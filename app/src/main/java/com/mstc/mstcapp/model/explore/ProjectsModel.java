package com.mstc.mstcapp.model.explore;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "PROJECTS")
public class ProjectsModel {

    @SerializedName("title")
    private final String title;
    @SerializedName("link")
    private final String link;
    @SerializedName("description")
    private final String description;
    @SerializedName("image")
    private final String image;

    @PrimaryKey
    @NonNull
    @SerializedName("_id")
    private String id;

    public ProjectsModel(String title, String link, String description, String image) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.image = image;
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

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }
}
