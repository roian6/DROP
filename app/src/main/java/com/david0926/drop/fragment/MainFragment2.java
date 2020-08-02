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
import com.david0926.drop.Retrofit.DROPRetrofit;
import com.david0926.drop.Retrofit.DROPRetrofitService;
import com.david0926.drop.LoginActivity;
import com.david0926.drop.R;
import com.david0926.drop.adapter.ArticleAdapter;
import com.david0926.drop.adapter.SocialGroupAdapter;
import com.david0926.drop.databinding.FragmentMain2Binding;
import com.david0926.drop.model.ArticleModel;
import com.david0926.drop.model.GroupModel;
import com.david0926.drop.model.UserModel;
import com.david0926.drop.util.LinearLayoutManagerWrapper;
import com.david0926.drop.util.TokenCache;
import com.david0926.drop.util.UserCache;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment2 extends Fragment {

    public static MainFragment2 newInstance() {
        return new MainFragment2();
    }

    private ObservableArrayList<GroupModel> groupItems = new ObservableArrayList<>();

    private Context mContext;
    private FragmentMain2Binding binding;

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main2, container, false);

        mContext.getTheme().applyStyle(R.style.AppTheme, true);

        LinearLayoutManagerWrapper wrapper = new LinearLayoutManagerWrapper(
                mContext, LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerMain2Group.setLayoutManager(wrapper);

        SocialGroupAdapter adapter = new SocialGroupAdapter();
        binding.recyclerMain2Group.setAdapter(adapter);
        binding.setGroupList(groupItems);

        adapter.setOnItemClickListener((view, item) -> {
            if(item.get_id()!=null && item.get_id().equals("add_group")){
                startActivity(new Intent(mContext, GroupActivity.class));
            }
            else{
                Intent intent = new Intent(mContext, GroupInfoActivity.class);
                intent.putExtra("group", item);
                startActivity(intent);
            }
        });
        adapter.setOnItemLongClickListener((view, item) -> true);

        return binding.getRoot();
    }

    @Override
    public void onResume() { // ㅎㅎ.. 이게 편해서 ^^..
        groupItems.clear();

        GroupModel addModel = new GroupModel();
        addModel.set_id("add_group");
        addModel.setName("그룹 추가");
        groupItems.add(addModel);

        DROPRetrofitService mRetrofitAPI = DROPRetrofit.getInstance(mContext).getDropService();
        System.out.println("T " + TokenCache.getToken(mContext).getAccess());
        Call<ResponseBody> mCallResponse = mRetrofitAPI.MyGroups(TokenCache.getToken(mContext).getAccess());
        mCallResponse.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String body = response.body().string();
                    System.out.println(body);
                    JSONObject object = new JSONObject(body);
                    JSONArray array = object.getJSONArray("data");

                    Gson gson = new Gson();

                    for(int i = 0; i < array.length(); i++) { // 최신순
                        JSONObject groupObject = array.getJSONObject(i);

                        GroupModel model = gson.fromJson(groupObject.toString(), GroupModel.class);

                        JSONObject creatorObject = groupObject.getJSONObject("creator");
                        model.setCreator(gson.fromJson(creatorObject.toString(), UserModel.class));

                        groupItems.add(model);
                    }

                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        ObservableArrayList<ArticleModel> articleItems = null;
        //gogo
        //get my articles and save to articleItems

        ArticleAdapter adapter = new ArticleAdapter();
        binding.recyclerMain2Myarticle.setAdapter(adapter);
        binding.setArticleList(articleItems);

        adapter.setOnItemClickListener((view, item) -> {
            Intent intent = new Intent(mContext, ArticleActivity.class);
            intent.putExtra("article", item);
            startActivity(intent);
        });
        adapter.setOnItemLongClickListener((view, item) -> true);

        super.onResume();
    }
}
