package com.mstc.mstcapp.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "FEED")
public class FeedModel {

    @SerializedName("title")
    private final String title;
    @SerializedName("description")
    private final String description;
    @SerializedName("link")
    private final String link;
    @SerializedName("image")
    private final String image;
    @SerializedName("createdAt")
    private final String createdAt;

    @PrimaryKey
    @NonNull
    @SerializedName("_id")
    private String id;

    public FeedModel(String title, String description, String link, String image, String createdAt) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.image = image;
        this.createdAt = createdAt;
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

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public String getImage() {
        return image;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
