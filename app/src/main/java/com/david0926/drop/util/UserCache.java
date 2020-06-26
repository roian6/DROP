package com.david0926.drop.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.david0926.drop.model.UserModel;
import com.google.gson.Gson;

public class UserCache {

    private static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setUser(Context context, String json) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("user_json", json).apply();
    }

    public static void logoutUser(Context context) {
        setUser(context, null);
    }

    public static boolean isLogoutUser(Context context) {
        return getUser(context) == null;
    }

    public static UserModel getUser(Context context) {

        try {
            Gson gson = new Gson();
            return gson.fromJson(getSharedPreferences(context).getString("user_json", ""), UserModel.class);
        } catch (Exception e) {
            return null;
        }
    }
}
