package com.david0926.drop.model;

import com.google.firebase.firestore.auth.User;

import java.io.Serializable;

public class CommentModel implements Serializable {

    private String time;
    private String _id;
    private String content;
    private Boolean isImportant;
    private UserModel user;

    public CommentModel() { }

    public CommentModel(String time, String _id, String content, Boolean isImportant, UserModel user) {
        this.time = time;
        this._id = _id;
        this.content = content;
        this.isImportant = isImportant;
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getIsImportant() {
        return isImportant;
    }

    public void setIsImportant(Boolean isImportant) {
        this.isImportant = isImportant;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
