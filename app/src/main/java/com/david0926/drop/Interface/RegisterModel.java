package com.david0926.drop.Interface;

import com.google.gson.annotations.SerializedName;

import okhttp3.MultipartBody;

public class RegisterModel {
    @SerializedName("userid")
    public String userid;


    @SerializedName("password")
    public String password;


    @SerializedName("name")
    public String name;

    @SerializedName("photo")
    public MultipartBody.Part photo;

    public RegisterModel(String userid, String password, String name, MultipartBody.Part photo) {
        this.photo = photo;
        this.userid = userid;
        this.password = password;
        this.name = name;
    }
}
