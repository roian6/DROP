package com.david0926.drop;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.david0926.drop.Interface.DROPRetrofitInterface;
import com.david0926.drop.adapter.ArticleAdapter;
import com.david0926.drop.databinding.ActivityGroupInfoBinding;
import com.david0926.drop.model.ArticleModel;
import com.david0926.drop.model.GroupModel;
import com.david0926.drop.util.LinearLayoutManagerWrapper;
import com.david0926.drop.util.TokenCache;
import com.david0926.drop.util.UserCache;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GroupInfoActivity extends AppCompatActivity {

    private ObservableArrayList<ArticleModel> articleItems = new ObservableArrayList<>();
    private ObservableArrayList<GroupModel> groupList = new ObservableArrayList<>();
    private GroupModel group;

    private ActivityGroupInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group_info);

        //finish activity, when back button pressed
        binding.toolbarGroupInfo.setNavigationOnClickListener(view -> finish());

        group = (GroupModel) getIntent().getSerializableExtra("group");
        binding.setGroup(group);
        binding.setUser(UserCache.getUser(this));

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
        adapter.setOnItemLongClickListener((view, item) -> true);

        if (UserCache.getUser(this).getGroup().contains(group.getId())) {
            Retrofit register = new Retrofit.Builder()
                    .baseUrl(getString(R.string.base_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            DROPRetrofitInterface mRetrofitAPI = register.create(DROPRetrofitInterface.class);
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
                        for(int i = array.length()-1; i >= 0; i--) { // 최신순
                            GroupModel model = gson.fromJson(array.getJSONObject(i).toString(), GroupModel.class);
                            groupList.add(model);
                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (group.getUser_id() == null) return false;

        MenuInflater inflater = getMenuInflater();
        if (group.getUser_id().equals(UserCache.getUser(this).getId())) { //admin
            inflater.inflate(R.menu.menu_group_info_admin, menu);
        } else if (groupList.contains(group)) { //member
            inflater.inflate(R.menu.menu_group_info_member, menu);
        }
        return true;
    }
}
