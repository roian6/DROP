package com.david0926.drop.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.david0926.drop.ArticleActivity;
import com.david0926.drop.Interface.DROPRetrofitInterface;
import com.david0926.drop.R;
import com.david0926.drop.adapter.ArticleAdapter;
import com.david0926.drop.databinding.FragmentMain1Binding;
import com.david0926.drop.model.ArticleModel;
import com.david0926.drop.model.CommentModel;
import com.david0926.drop.model.GroupModel;
import com.david0926.drop.model.UserModel;
import com.david0926.drop.util.LinearLayoutManagerWrapper;
import com.david0926.drop.util.TokenCache;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainFragment1 extends Fragment {

    public static MainFragment1 newInstance() {
        return new MainFragment1();
    }

    private ObservableArrayList<ArticleModel> articleItems = new ObservableArrayList<>();

    private Context mContext;
    private FragmentMain1Binding binding;

    int offset = 10;
    private boolean isNowsearching = false;

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main1, container, false);
        binding.setIsEmpty(false);

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

        binding.btnMain1Search.setOnClickListener(view -> { // 검색 기능
            if(!binding.edtMain1Search.getText().toString().trim().isEmpty()) { // 비어있지 않다면
                articleItems.clear();
                isNowsearching = true; // 검색모드 활성화
                offset = 10; // offset 초기화
                refreshPost(0, binding.edtMain1Search.getText().toString().trim());
            } else {
                articleItems.clear();
                isNowsearching = false; // 검색모드 비활성화
                offset = 10; // offset 초기화
                refreshPost(0, "");
            }
            // 빈 상태에서 누르면 그냥 새로고침

        });

        binding.edtMain1Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().isEmpty()) { // 검색창이 비었을때 즉시 검색모드 비활성화하고 새로고침
                    articleItems.clear();
                    isNowsearching = false; // 검색모드 비활성화
                    offset = 10; // offset 초기화
                    refreshPost(0, "");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.edtMain1Search.setOnEditorActionListener((textView, i, keyEvent) -> {
            if(i== EditorInfo.IME_ACTION_SEARCH){

                if(!binding.edtMain1Search.getText().toString().trim().isEmpty()) { // 비어있지 않다면
                    articleItems.clear();
                    isNowsearching = true; // 검색모드 활성화
                    offset = 10; // offset 초기화
                    refreshPost(0, binding.edtMain1Search.getText().toString().trim());
                } else {
                    articleItems.clear();
                    isNowsearching = false; // 검색모드 비활성화
                    offset = 10; // offset 초기화
                    refreshPost(0, "");
                }
                // 빈 상태에서 누르면 그냥 새로고침

                return true;
            }
            return false;
        });

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
                if (lastPosition == totalCount - 1 && totalCount % 10 == 0) {
                    if(isNowsearching) // 검색모드라면
                        refreshPost(offset, binding.edtMain1Search.getText().toString()); // 입력해둔 검색 키워드로 페이지네이션
                    else // 검색모드가 아니라면
                        refreshPost(offset, ""); // 계속 쭊쭊
                    offset += 10;
                }
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        //binding.edtMain1Search.setText("");
        articleItems.clear();
        isNowsearching = false; // 검색모드 비활성화
        offset = 10; // offset 초기화
        refreshPost(0, "");
        super.onResume();
    }

    private void refreshPost(int length, String keyword) {

        Retrofit register = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DROPRetrofitInterface mRetrofitAPI = register.create(DROPRetrofitInterface.class);

        Call<ResponseBody> mCallResponse = mRetrofitAPI.getPosts(TokenCache.getToken(mContext).getAccess(), length, keyword);
        mCallResponse.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String body = response.body().string();
                    JSONObject object = new JSONObject(body).getJSONObject("data");

                    int count = object.getInt("count");
                    object.remove("count");

                    JSONArray array = object.toJSONArray(object.names());

                    if(!isNowsearching&&array==null&&articleItems.isEmpty()) binding.setIsEmpty(true);
                    else binding.setIsEmpty(false);

                    object.put("count", count);


                    Gson gson = new Gson();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        System.out.println("POST : " + obj.toString() + "\n\n");
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
                        for (int j = c_array.length()-1; j >= 0; j--) { // 최신순
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
