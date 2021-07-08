package com.mstc.mstcapp.model;

import com.google.gson.annotations.SerializedName;

public class ProjectIdeaModel {
    @SerializedName("name")
    public String name;
    @SerializedName("phone")
    public String phone;
    @SerializedName("email")
    public String email;
    @SerializedName("idea")
    public String idea;
    @SerializedName("description")
    public String description;

    public ProjectIdeaModel(String name, String phone, String email, String idea, String description) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.idea = idea;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdea() {
        return idea;
    }

    public void setIdea(String idea) {
        this.idea = idea;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
