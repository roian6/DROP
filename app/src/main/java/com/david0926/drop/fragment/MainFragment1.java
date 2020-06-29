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
import androidx.recyclerview.widget.RecyclerView;

import com.allattentionhere.fabulousfilter.AAH_FabulousFragment;
import com.david0926.drop.ArticleActivity;
import com.david0926.drop.Interface.DROPRetrofitInterface;
import com.david0926.drop.R;
import com.david0926.drop.adapter.ArticleAdapter;
import com.david0926.drop.databinding.FragmentMain1Binding;
import com.david0926.drop.model.ArticleModel;
import com.david0926.drop.model.CommentModel;
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

public class MainFragment1 extends Fragment{

    public static MainFragment1 newInstance() {
        return new MainFragment1();
    }

    private ObservableArrayList<ArticleModel> articleItems = new ObservableArrayList<>();

    private Context mContext;
    private FragmentMain1Binding binding;

    int offset = 10;

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main1, container, false);

        LinearLayoutManagerWrapper wrapper = new LinearLayoutManagerWrapper(
                mContext, LinearLayoutManager.VERTICAL, false);
        binding.recyclerMain1.setLayoutManager(wrapper);

        ArticleAdapter adapter = new ArticleAdapter();
        binding.recyclerMain1.setAdapter(adapter);
        binding.setArticleList(articleItems);

        adapter.setOnItemClickListener((view, item) -> {
            Intent intent = new Intent(mContext, ArticleActivity.class);
            intent.putExtra("article", item);
            startActivity(intent);
        });
        adapter.setOnItemLongClickListener((view, item) -> true);

        binding.recyclerMain1.addOnScrollListener(new RecyclerView.OnScrollListener() { // 페이지네이션
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int totalCount = recyclerView.getAdapter().getItemCount();
                if(lastPosition == totalCount-1 && totalCount % 10 == 0){
                    refreshPost(offset);
                    offset += 10;
                }
            }
        });

        refreshPost(0);
        return binding.getRoot();
    }

    private void refreshPost(int length) {


            Retrofit register = new Retrofit.Builder()
                    .baseUrl(getString(R.string.base_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            DROPRetrofitInterface mRetrofitAPI = register.create(DROPRetrofitInterface.class);

            Call<ResponseBody> mCallResponse = mRetrofitAPI.getPosts(TokenCache.getToken(mContext).getAccess(), length);
            mCallResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String body = response.body().string();
                        JSONObject object = new JSONObject(body);
                        JSONArray array = object.getJSONArray("data");

                        Gson gson = new Gson();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            ArticleModel am = new ArticleModel();
                            am.setGroup_id(obj.getString("group"));
                            am.setId(obj.getString("_id"));

                            am.setType(obj.getString("type"));
                            am.setSolve(obj.getBoolean("isResolved"));

//                            am.setUser_email();
//                            am.setUser_name();
//                            am.setUser_profile();

                            am.setProduct_addinfo(obj.getString("reward"));
                            try {
                                am.setProduct_desc(obj.getString("description"));
                            } catch(Exception e) {
                                am.setProduct_desc("설명이 존재하지 않습니다");
                            }
                            am.setProduct_image(obj.getString("photo"));
                            am.setProduct_name(obj.getString("title"));
                            am.setProduct_place(obj.getString("place"));
                            am.setProduct_time(obj.getString("time"));

//                            am.setUpload_time("uploadTime");

                            JSONArray c_array = obj.getJSONArray("comment");
                            ArrayList<CommentModel> c_list = new ArrayList<>();
                            for(int j = c_array.length() - 1; j >= 0; j--) { // 최신순
                                CommentModel cm = gson.fromJson(c_array.getJSONObject(i).toString(), CommentModel.class);
                                c_list.add(cm);
                            }
                            am.setComments(c_list);
                            articleItems.add(am);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });


    }

}
