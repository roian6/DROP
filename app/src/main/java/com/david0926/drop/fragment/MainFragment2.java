package com.david0926.drop.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
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
import com.david0926.drop.Interface.DROPRetrofitInterface;
import com.david0926.drop.Interface.LoginModel;
import com.david0926.drop.LoginActivity;
import com.david0926.drop.R;
import com.david0926.drop.adapter.ArticleAdapter;
import com.david0926.drop.adapter.SocialGroupAdapter;
import com.david0926.drop.databinding.FragmentMain2Binding;
import com.david0926.drop.model.GroupModel;
import com.david0926.drop.model.UserModel;
import com.david0926.drop.util.LinearLayoutManagerWrapper;
import com.david0926.drop.util.TokenCache;
import com.david0926.drop.util.UserCache;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

        binding.setUser(UserCache.getUser(mContext));

        LinearLayoutManagerWrapper wrapper = new LinearLayoutManagerWrapper(
                mContext, LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerMain2Group.setLayoutManager(wrapper);

        SocialGroupAdapter adapter = new SocialGroupAdapter();
        binding.recyclerMain2Group.setAdapter(adapter);
        binding.setGroupList(groupItems);

        adapter.setOnItemClickListener((view, item) -> {
            if(item.getId().equals("add_group")){
                startActivity(new Intent(mContext, GroupActivity.class));
            }
        });
        adapter.setOnItemLongClickListener((view, item) -> true);

        GroupModel addModel = new GroupModel();
        addModel.setId("add_group");
        addModel.setName("그룹 추가");

//        GroupModel model = new GroupModel();
//        model.setName("선린인터넷고등학교 분실물센터");
//        model.setImage(getString(R.string.test_image));
//
//        GroupModel model2 = new GroupModel();
//        model2.setName("서울지하철 분실물센터");
//        model2.setImage(getString(R.string.test_image));
//
//        GroupModel model3 = new GroupModel();
//        model3.setName("위워크 분실물센터");
//        model3.setImage(getString(R.string.test_image));
//
//        GroupModel model4 = new GroupModel();
//        model4.setName("정신줄 관리센터");
//        model4.setImage(getString(R.string.test_image));
//

          groupItems.add(addModel);
//        groupItems.add(model);
//        groupItems.add(model2);
//        groupItems.add(model3);
//        groupItems.add(model4);


        Retrofit register = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DROPRetrofitInterface mRetrofitAPI = register.create(DROPRetrofitInterface.class);
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



                    for(int i = 0; i < array.length(); i++) {
                        GroupModel model = new GroupModel();
                        model.setName(array.getJSONObject(i).getString("name"));
                        model.setId(array.getJSONObject(i).getString("_id"));
//                        model.setImage(array.getJSONObject(i).getString("photo"));
                        model.setImage(getString(R.string.test_image));
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



        return binding.getRoot();
    }

}
