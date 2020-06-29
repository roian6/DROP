package com.david0926.drop.model;

import java.io.Serializable;

public class GroupModel implements Serializable {

    private String id, user_id, name, photo, description, v;

    public GroupModel() {
    }

    public GroupModel(String id, String user_id, String name, String photo, String description, String v) {
        this.id = id;
        this.user_id = user_id;
        this.name = name;
        this.photo = photo;
        this.description = description;
        this.v = v;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }
}
