package com.david0926.drop.model;

import java.io.Serializable;

public class CommentModel implements Serializable {

    private String user_name, user_email, user_profile, upload_time, text;
    private Boolean important;

    public CommentModel() { }

    public CommentModel(String user_name, String user_email, String user_profile, String upload_time, String text, Boolean important) {
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_profile = user_profile;
        this.upload_time = upload_time;
        this.text = text;
        this.important = important;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_profile() {
        return user_profile;
    }

    public void setUser_profile(String user_profile) {
        this.user_profile = user_profile;
    }

    public String getUpload_time() {
        return upload_time;
    }

    public void setUpload_time(String upload_time) {
        this.upload_time = upload_time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getImportant() {
        return important;
    }

    public void setImportant(Boolean important) {
        this.important = important;
    }
}
