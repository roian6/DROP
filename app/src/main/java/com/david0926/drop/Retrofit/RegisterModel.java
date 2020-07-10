package com.david0926.drop.Retrofit;

import com.google.gson.annotations.SerializedName;

import okhttp3.MultipartBody;

public class RegisterModel {
    @SerializedName("userid")
    public String userid;


    @SerializedName("password")
    public String password;

    @SerializedName("name")
    public String name;

    @SerializedName("fcmtoken")
    public String fcmtoken;

    @SerializedName("photo")
    public MultipartBody.Part photo;

    public RegisterModel(String userid, String password, String name, String fcmtoken, MultipartBody.Part photo) {
        this.photo = photo;
        this.userid = userid;
        this.password = password;
        this.name = name;
        this.fcmtoken = fcmtoken;
    }
}
