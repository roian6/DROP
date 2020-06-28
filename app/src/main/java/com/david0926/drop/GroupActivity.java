package com.david0926.drop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.david0926.drop.Interface.DROPRetrofitInterface;
import com.david0926.drop.adapter.GroupAdapter;
import com.david0926.drop.databinding.ActivityGroupBinding;
import com.david0926.drop.model.GroupModel;
import com.david0926.drop.util.LinearLayoutManagerWrapper;
import com.david0926.drop.util.TokenCache;

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
//            Intent intent = new Intent(this, ArticleActivity.class);
//            intent.putExtra("article", item);
//            startActivity(intent);
        });
        adapter.setOnItemLongClickListener((view, item) -> true);

        binding.btnGroupNew.setOnClickListener(view ->
                startActivity(new Intent(GroupActivity.this, GroupNewActivity.class)));

    }

    @Override
    protected void onResume() {
        groupItems.clear();
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



                    for(int i = array.length()-1; i >= 0; i--) { // 최신순
                        GroupModel model = new GroupModel();
                        model.setName(array.getJSONObject(i).getString("name"));
                        model.setId(array.getJSONObject(i).getString("_id"));
                        try {
                            model.setPhoto(array.getJSONObject(i).getString("photo"));
                        } catch(Exception err){
                            model.setPhoto(getString(R.string.test_image));
                        }
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

        super.onResume();
    }
}
