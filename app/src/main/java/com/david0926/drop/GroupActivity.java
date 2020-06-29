package com.david0926.drop;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.david0926.drop.Interface.DROPRetrofitInterface;
import com.david0926.drop.adapter.GroupAdapter;
import com.david0926.drop.databinding.ActivityGroupBinding;
import com.david0926.drop.model.GroupModel;
import com.david0926.drop.util.LinearLayoutManagerWrapper;
import com.david0926.drop.util.TokenCache;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GroupActivity extends AppCompatActivity {

    private ObservableArrayList<GroupModel> groupItems = new ObservableArrayList<>();

    private ActivityGroupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group);

        //finish activity, when back button pressed
        binding.toolbarGroup.setNavigationOnClickListener(view -> finish());

        LinearLayoutManagerWrapper wrapper = new LinearLayoutManagerWrapper(
                this, LinearLayoutManager.VERTICAL, false);
        binding.recyclerGroup.setLayoutManager(wrapper);

        GroupAdapter adapter = new GroupAdapter();
        binding.recyclerGroup.setAdapter(adapter);
        binding.setGroupList(groupItems);

        adapter.setOnItemClickListener((view, item) -> {
            Intent intent = new Intent(GroupActivity.this, GroupInfoActivity.class);
            intent.putExtra("group", item);
            startActivity(intent);
        });
        adapter.setOnItemLongClickListener((view, item) -> true);

        binding.btnGroupNew.setOnClickListener(view ->
                startActivity(new Intent(GroupActivity.this, GroupNewActivity.class)));
    }

    @Override
    protected void onResume() {
        groupItems.clear();
        loadGroup();

        super.onResume();
    }

    void loadGroup() {
        Retrofit register = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DROPRetrofitInterface mRetrofitAPI = register.create(DROPRetrofitInterface.class);
        Call<ResponseBody> mCallResponse = mRetrofitAPI.getGroups(TokenCache.getToken(this).getAccess());
        mCallResponse.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String body = response.body().string();
                    System.out.println(body);
                    JSONObject object = new JSONObject(body);
                    JSONArray array = object.getJSONArray("data");

                    Gson gson = new Gson();

                    for (int i = array.length() - 1; i >= 0; i--) { // 최신순
                        GroupModel model = gson.fromJson(array.getJSONObject(i).toString(), GroupModel.class);
                        groupItems.add(model);
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
