package com.david0926.drop.model;

import java.io.Serializable;

public class GroupModel implements Serializable {

    private String _id, creator, name, photo, description, v;

    public GroupModel() {
    }

    public GroupModel(String _id, String creator, String name, String photo, String description, String v) {
        this._id = _id;
        this.creator = creator;
        this.name = name;
        this.photo = photo;
        this.description = description;
        this.v = v;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
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
