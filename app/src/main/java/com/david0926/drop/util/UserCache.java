package com.david0926.drop.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.david0926.drop.model.UserModel;

public class UserCache {

    private static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setUser(Context context, UserModel model) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor
                .putString("user_name", model.getName())
                .putString("user_email", model.getEmail())
                .putString("user_profile", model.getProfile())
                .apply();
    }

    public static UserModel getUser(Context context) {
        UserModel model = new UserModel();
        model.setName(getSharedPreferences(context).getString("user_name", "username"));
        model.setEmail(getSharedPreferences(context).getString("user_email", "email@email.com"));
        model.setProfile(getSharedPreferences(context).getString("user_profile", ""));
        return model;
    }
}
