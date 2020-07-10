package com.david0926.drop.Retrofit;

import android.content.Context;

import com.david0926.drop.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DROPRetrofit {

    private static DROPRetrofit instance;

    private DROPRetrofitService dropService;

    public static DROPRetrofit getInstance(Context context) {
        if (instance == null) {
            instance = new DROPRetrofit(context);
        }
        return instance;
    }

    private DROPRetrofit(Context context) {
        Retrofit register = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        dropService = register.create(DROPRetrofitService.class);
    }

    public DROPRetrofitService getDropService() {
        return dropService;
    }
}
