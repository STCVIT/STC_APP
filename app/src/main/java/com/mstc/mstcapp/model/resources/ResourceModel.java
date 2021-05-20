package com.mstc.mstcapp.model.resources;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "RESOURCES")

public class ResourceModel {

    @SerializedName("title")
    private final String title;
    @SerializedName("link")
    private final String link;
    @SerializedName("description")
    private final String description;
    @SerializedName("domain")
    private final String domain;

    @PrimaryKey
    @NonNull
    @SerializedName("_id")
    private String id;

    public ResourceModel(String title, String link, String description, String domain) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.domain = domain;
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

    public String getDomain() {
        return domain;
    }

}
