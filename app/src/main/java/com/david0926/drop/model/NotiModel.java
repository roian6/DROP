package com.david0926.drop.model;

import java.io.Serializable;
import java.util.ArrayList;

public class NotiModel implements Serializable {

    private ArrayList<UserModel> targetList;
    private ArticleModel post;
    private String title, content, time;

    public NotiModel(){}

    public NotiModel(ArrayList<UserModel> targetList, ArticleModel post, String title, String content, String time) {
        this.targetList = targetList;
        this.post = post;
        this.title = title;
        this.content = content;
        this.time = time;
    }

    public ArrayList<UserModel> getTargetList() {
        return targetList;
    }

    public void setTargetList(ArrayList<UserModel> targetList) {
        this.targetList = targetList;
    }

    public ArticleModel getPost() {
        return post;
    }

    public void setPost(ArticleModel post) {
        this.post = post;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
