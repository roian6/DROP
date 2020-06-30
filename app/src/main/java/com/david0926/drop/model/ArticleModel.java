package com.david0926.drop.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.david0926.drop.BR;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ArticleModel extends BaseObservable implements Serializable {

    private Boolean isResolved;
    private String photo;
    private String reward;
    private ArrayList<CommentModel> comment;
    private String _id;
    private String title;
    private String description;
    private String type;
    private String time;
    private String place;
    private GroupModel group;
    private UserModel user;
    private String uploadTime;
    private Integer __v;

    public ArticleModel() {
    }

    public ArticleModel(Boolean isResolved, String photo, String reward, ArrayList<CommentModel> comment, String _id, String title, String description, String type, String time, String place, GroupModel group, UserModel user, String uploadTime, Integer __v) {
        this.isResolved = isResolved;
        this.photo = photo;
        this.reward = reward;
        this.comment = comment;
        this._id = _id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.time = time;
        this.place = place;
        this.group = group;
        this.user = user;
        this.uploadTime = uploadTime;
        this.__v = __v;
    }

    public Boolean getResolved() {
        return isResolved;
    }

    public void setResolved(Boolean resolved) {
        isResolved = resolved;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Bindable
    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        notifyPropertyChanged(BR.reward);
        this.reward = reward;
    }

    public ArrayList<CommentModel> getComment() {
        return comment;
    }

    public void setComment(ArrayList<CommentModel> comment) {
        this.comment = comment;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        notifyPropertyChanged(BR.title);
        this.title = title;
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        notifyPropertyChanged(BR.description);
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Bindable
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        notifyPropertyChanged(BR.time);
        this.time = time;
    }

    @Bindable
    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        notifyPropertyChanged(BR.place);
        this.place = place;
    }

    public GroupModel getGroup() {
        return group;
    }

    public void setGroup(GroupModel group) {
        this.group = group;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Integer get__v() {
        return __v;
    }

    public void set__v(Integer __v) {
        this.__v = __v;
    }
}

