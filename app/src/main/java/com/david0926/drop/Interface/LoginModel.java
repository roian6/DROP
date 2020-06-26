package com.david0926.drop.Interface;

import com.google.gson.annotations.SerializedName;

// Author : 최종수

public class LoginModel {
    @SerializedName("userid")
    public String userid;

    @SerializedName("password")
    public String password;

    public LoginModel(String userid, String password) {
        this.userid = userid;
        this.password = password;
    }
}
