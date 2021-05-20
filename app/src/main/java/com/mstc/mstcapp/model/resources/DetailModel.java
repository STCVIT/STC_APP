package com.mstc.mstcapp.model.resources;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "DETAILS")
public class DetailModel {
    @SerializedName("description")
    private final String description;
    @SerializedName("domain")
    private final String domain;
    @SerializedName("expectation")
    private final String expectation;

    @PrimaryKey
    @NonNull
    @SerializedName("_id")
    private String id;

    public DetailModel(String description, String domain, String expectation) {
        this.description = description;
        this.domain = domain;
        this.expectation = expectation;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getDomain() {
        return domain;
    }

    public String getExpectation() {
        return expectation;
    }
}
