package com.mstc.mstcapp.model.resources;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "ROADMAPS")
public class RoadmapModel {

    @SerializedName("domain")
    private final String domain;
    @SerializedName("image")
    private final String image;

    @PrimaryKey
    @NonNull
    @SerializedName("_id")
    private String id;

    public RoadmapModel(String domain, String image) {
        this.domain = domain;
        this.image = image;
    }

    @NotNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getDomain() {
        return domain;
    }

    public String getImage() {
        return image;
    }
}
