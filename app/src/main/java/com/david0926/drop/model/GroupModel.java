package com.david0926.drop.model;

import java.io.Serializable;

public class GroupModel implements Serializable {

    private String id, name, photo, description, v;

    public GroupModel() {
    }

    public GroupModel(String id, String name, String photo, String description, String v) {
        this.id = id;
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
