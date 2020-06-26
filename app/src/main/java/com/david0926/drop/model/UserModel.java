package com.david0926.drop.model;

import java.util.List;

public class UserModel {

    private String authority;
    private List<String> group;
    private String id;
    private String userid;
    private String password;
    private String enckey;
    private String name;
    private String photo;
    private Integer v;

    public UserModel(String authority, List<String> group, String id, String userid, String password, String enckey, String name, String photo, Integer v) {
        this.authority = authority;
        this.group = group;
        this.id = id;
        this.userid = userid;
        this.password = password;
        this.enckey = enckey;
        this.name = name;
        this.photo = photo;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }
}
