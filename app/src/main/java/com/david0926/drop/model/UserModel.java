package com.david0926.drop.model;

import java.io.Serializable;
import java.util.List;

public class UserModel implements Serializable {

    private String authority;
    private List<String> group;
    private String _id;
    private String userid;
    private String password;
    private String enckey;
    private String name;
    private String photo;
    private String fcmtoken;
    private Integer v;

    public UserModel(){}

    public UserModel(String authority, List<String> group, String _id, String userid, String password, String enckey, String name, String photo, String fcmtoken, Integer v) {
        this.authority = authority;
        this.group = group;
        this._id = _id;
        this.userid = userid;
        this.password = password;
        this.enckey = enckey;
        this.name = name;
        this.photo = photo;
        this.fcmtoken = fcmtoken;
        this.v = v;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public List<String> getGroup() {
        return group;
    }

    public void setGroup(List<String> group) {
        this.group = group;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEnckey() {
        return enckey;
    }

    public void setEnckey(String enckey) {
        this.enckey = enckey;
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

    public String getFcmtoken() {
        return fcmtoken;
    }

    public void setFcmtoken(String fcmtoken) {
        this.fcmtoken = fcmtoken;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }
}
