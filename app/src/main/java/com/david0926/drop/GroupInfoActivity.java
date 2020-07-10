package com.david0926.drop;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.david0926.drop.Retrofit.DROPRetrofit;
import com.david0926.drop.Retrofit.DROPRetrofitService;
import com.david0926.drop.adapter.ArticleAdapter;
import com.david0926.drop.databinding.ActivityGroupInfoBinding;
import com.david0926.drop.model.ArticleModel;
import com.david0926.drop.model.CommentModel;
import com.david0926.drop.model.GroupModel;
import com.david0926.drop.model.UserModel;
import com.david0926.drop.util.LinearLayoutManagerWrapper;
import com.david0926.drop.util.TokenCache;
import com.google.gson.Gson;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupInfoActivity extends AppCompatActivity {

    private ObservableArrayList<ArticleModel> articleItems = new ObservableArrayList<>();
    private ObservableArrayList<GroupModel> groupList = new ObservableArrayList<>();
    private GroupModel group;

    private ActivityGroupInfoBinding binding;

    int offset = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group_info);
        binding.setIsMember(false);

        Logger.addLogAdapter(new AndroidLogAdapter());

        setSupportActionBar(binding.toolbarGroupInfo);

        //finish activity, when back button pressed
        binding.toolbarGroupInfo.setNavigationOnClickListener(view -> finish());

        group = (GroupModel) getIntent().getSerializableExtra("group");
        binding.setGroup(group);

        LinearLayoutManagerWrapper wrapper = new LinearLayoutManagerWrapper(
                this, LinearLayoutManager.VERTICAL, false);
        binding.recyclerGroupInfo.setLayoutManager(wrapper);

        ArticleAdapter adapter = new ArticleAdapter();
        binding.recyclerGroupInfo.setAdapter(adapter);
        binding.setArticleList(articleItems);

        adapter.setOnItemClickListener((view, item) -> {
            Intent intent = new Intent(this, ArticleActivity.class);
            intent.putExtra("article", item);
            startActivity(intent);
        });

        binding.btnGroupInfoJoin.setOnClickListener(view -> {
            joinGroup(group.get_id());
        });


        adapter.setOnItemLongClickListener((view, item) -> true);

        DROPRetrofitService mRetrofitAPI = DROPRetrofit.getInstance(this).getDropService();
        System.out.println("T " + TokenCache.getToken(this).getAccess());
        Call<ResponseBody> mCallResponse = mRetrofitAPI.MyGroups(TokenCache.getToken(this).getAccess());
        mCallResponse.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String body = response.body().string();
                    System.out.println(body);
                    JSONObject object = new JSONObject(body);
                    JSONArray array = object.getJSONArray("data");

                    Gson gson = new Gson();
                    for (int i = 0; i < array.length(); i++) { // 최신순
                        JSONObject groupObject = array.getJSONObject(i);
                        GroupModel groupModel = gson.fromJson(groupObject.toString(), GroupModel.class);

                        JSONObject creatorObject = groupObject.getJSONObject("creator");
                        groupModel.setCreator(gson.fromJson(creatorObject.toString(), UserModel.class));

                        groupList.add(groupModel);
                    }
                    setMemberState();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        binding.recyclerGroupInfo.addOnScrollListener(new RecyclerView.OnScrollListener() { // 페이지네이션
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int totalCount = recyclerView.getAdapter().getItemCount();
                System.out.println(lastPosition + " : " + totalCount);
                if(lastPosition == totalCount-1 && totalCount % 10 == 0){
                    System.out.println("새로고침");
                    refreshPost(offset);
                    offset += 10;
                }
            }
        });

    }

    private void refreshPost(int length) {

        if(binding.getIsMember()) { // 만약 그룹 멤버일 경우 게시글 표시

            DROPRetrofitService mRetrofitAPI = DROPRetrofit.getInstance(this).getDropService();

            Call<ResponseBody> mCallResponse = mRetrofitAPI.getPosts(TokenCache.getToken(this).getAccess(), group.get_id(), length);
            System.out.println("Group : " + group.get_id());
            mCallResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String body = response.body().string();
                        JSONObject object = new JSONObject(body).getJSONObject("data");

                        int count = object.getInt("count");
                        object.remove("count");

                        JSONArray array = object.toJSONArray(object.names());

                        object.put("count", count);

                        Gson gson = new Gson();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            ArticleModel am = gson.fromJson(obj.toString(), ArticleModel.class);

                            JSONObject userObject = obj.getJSONObject("user");
                            am.setUser(gson.fromJson(userObject.toString(), UserModel.class));

                            JSONObject groupObject = obj.getJSONObject("group");
                            GroupModel groupModel = gson.fromJson(groupObject.toString(), GroupModel.class);

                            JSONObject creatorObject = groupObject.getJSONObject("creator");
                            groupModel.setCreator(gson.fromJson(creatorObject.toString(), UserModel.class));

                            am.setGroup(groupModel);

                            JSONArray c_array = obj.getJSONArray("comment");
                            ArrayList<CommentModel> c_list = new ArrayList<>();
                            for(int j = c_array.length() - 1; j >= 0; j--) { // 최신순
                                CommentModel cm = gson.fromJson(c_array.getJSONObject(j).toString(), CommentModel.class);
                                c_list.add(cm);
                            }

                            am.setComment(c_list);
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

    private void joinGroup(String id) {

        DROPRetrofitService mRetrofitAPI = DROPRetrofit.getInstance(this).getDropService();

        RequestBody idbody = RequestBody.create(MediaType.parse("multipart/form-data"), id);

        Call<ResponseBody> mCallResponse = mRetrofitAPI.JoinGroup(TokenCache.getToken(this).getAccess(), idbody);
        mCallResponse.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String body = response.body().string();
                    finish();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }

    void setMemberState() {

        for(GroupModel model:groupList){
            if(model.get_id().equals(group.get_id())) binding.setIsMember(true);
        }

        refreshPost(0);
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_group_info, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

//        if (group.getCreator().get_id().equals(UserCache.getUser(this).get_id())) { //admin
//            menu.findItem(R.id.action_delete).setVisible(true);
//            menu.findItem(R.id.action_edit).setVisible(true);
//        } else if (binding.getIsMember()) { //member
//            menu.findItem(R.id.action_exit).setVisible(true);
//        }
        return super.onPrepareOptionsMenu(menu);
    }
}
