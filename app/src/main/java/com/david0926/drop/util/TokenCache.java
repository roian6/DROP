package com.david0926.drop.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.david0926.drop.model.TokenModel;
import com.google.gson.Gson;

public class TokenCache {

    private static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setToken(Context context, String json) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("token_json", json).apply();
    }

    public static TokenModel getToken(Context context) {

        try {
            Gson gson = new Gson();
            return gson.fromJson(getSharedPreferences(context).getString("token_json", ""), TokenModel.class);
        } catch (Exception e) {
            return null;
        }
    }
}
