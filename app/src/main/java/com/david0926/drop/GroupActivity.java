package com.david0926.drop;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.david0926.drop.Retrofit.DROPRetrofit;
import com.david0926.drop.Retrofit.DROPRetrofitService;
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

public class GroupActivity extends AppCompatActivity {

    private ObservableArrayList<GroupModel> groupItems = new ObservableArrayList<>();
    private ObservableArrayList<GroupModel> groupItemsCache = new ObservableArrayList<>();

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

        binding.edtGroupSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                groupItems.clear();
                for(GroupModel model:groupItemsCache){
                    if(model.getName().contains(charSequence)) groupItems.add(model);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void onResume() {
        loadGroup();
        super.onResume();
    }

    void loadGroup() {
        groupItems.clear();
        groupItemsCache.clear();

        DROPRetrofitService mRetrofitAPI = DROPRetrofit.getInstance(this).getDropService();
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
                    groupItemsCache.addAll(groupItems);

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
