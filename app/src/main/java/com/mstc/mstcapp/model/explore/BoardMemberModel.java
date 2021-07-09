package com.mstc.mstcapp.model.explore;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "BOARD")
public class BoardMemberModel {

    @SerializedName("photo")
    private final String photo;
    @SerializedName("name")
    private final String name;
    @SerializedName("position")
    private final String position;
    @SerializedName("linkedIn")
    private final String link;
    @SerializedName("phrase")
    private final String phrase;

    @PrimaryKey
    @NonNull
    @SerializedName("_id")
    private String id;

    public BoardMemberModel(String photo, String name, String position, String phrase, String link) {
        this.photo = photo;
        this.name = name;
        this.position = position;
        this.phrase = phrase;
        this.link = link;
    }

    @NotNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public String getLink() {
        return link;
    }

    public String getPhrase() {
        return phrase;
    }
}
