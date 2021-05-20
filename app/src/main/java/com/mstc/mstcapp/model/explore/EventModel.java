package com.mstc.mstcapp.model.explore;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "EVENTS")
public class EventModel {
    @SerializedName("title")
    private final String title;
    @SerializedName("image")
    private final String image;
    @SerializedName("link")
    private final String link;
    @SerializedName("description")
    private final String description;
    @SerializedName("status")
    private final String status;
    @SerializedName("startDate")
    private final String startDate;
    @SerializedName("endDate")
    private final String endDate;
    @SerializedName("createdAt")
    private final String createdAt;

    @PrimaryKey
    @NonNull
    @SerializedName("_id")
    public String id;

    public EventModel(String title, String description, String link, String image, String status, String startDate, String endDate, String createdAt) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.image = image;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public String getEndDate() {
        return endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
