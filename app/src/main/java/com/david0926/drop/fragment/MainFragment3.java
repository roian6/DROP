package com.david0926.drop.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.david0926.drop.ArticleActivity;
import com.david0926.drop.GroupActivity;
import com.david0926.drop.GroupInfoActivity;
import com.david0926.drop.Interface.DROPRetrofitInterface;
import com.david0926.drop.R;
import com.david0926.drop.adapter.GroupAdapter;
import com.david0926.drop.adapter.NotiAdapter;
import com.david0926.drop.databinding.FragmentMain3Binding;
import com.david0926.drop.model.ArticleModel;
import com.david0926.drop.model.CommentModel;
import com.david0926.drop.model.GroupModel;
import com.david0926.drop.model.NotiModel;
import com.david0926.drop.model.UserModel;
import com.david0926.drop.util.LinearLayoutManagerWrapper;
import com.david0926.drop.util.TokenCache;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainFragment3 extends Fragment {

    public static MainFragment3 newInstance() {
        return new MainFragment3();
    }

    private ObservableArrayList<NotiModel> notiItems = new ObservableArrayList<>();

    private Context mContext;
    private FragmentMain3Binding binding;

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main3, container, false);

        LinearLayoutManagerWrapper wrapper = new LinearLayoutManagerWrapper(
                mContext, LinearLayoutManager.VERTICAL, false);
        binding.recyclerNoti.setLayoutManager(wrapper);

        NotiAdapter adapter = new NotiAdapter();
        binding.recyclerNoti.setAdapter(adapter);
        binding.setNotiList(notiItems);

        adapter.setOnItemClickListener((view, item) -> {
            Intent intent = new Intent(mContext, ArticleActivity.class);
            intent.putExtra("article", item.getPost());
            intent.putExtra("to_comment", true);
            startActivity(intent);
        });
        adapter.setOnItemLongClickListener((view, item) -> true);

//        NotiModel model = new NotiModel();
//        model.setContent("sadasd");
//        model.setTime("2020/07/01 16:00:00");

//        notiItems.add(model);

        //종수: 노티 가져와서 리스트에 넣기

        Retrofit register = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DROPRetrofitInterface mRetrofitAPI = register.create(DROPRetrofitInterface.class);

        Call<ResponseBody> mCallResponse = mRetrofitAPI.getNotification(TokenCache.getToken(mContext).getAccess());
        mCallResponse.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String body = response.body().string();
                    JSONObject obj = new JSONObject(body);
                    JSONArray arr = obj.getJSONArray("data");
                    System.out.println("노티바디 : " + body);
                    for(int i = 0; i < arr.length(); i++) {
                        NotiModel nm = new Gson().fromJson(arr.getJSONObject(i).toString(), NotiModel.class);
                        notiItems.add(nm);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        return binding.getRoot();
    }

}
