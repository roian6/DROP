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
import com.david0926.drop.R;
import com.david0926.drop.adapter.ArticleAdapter;
import com.david0926.drop.adapter.SocialGroupAdapter;
import com.david0926.drop.databinding.FragmentMain2Binding;
import com.david0926.drop.model.GroupModel;
import com.david0926.drop.model.UserModel;
import com.david0926.drop.util.LinearLayoutManagerWrapper;

import org.jetbrains.annotations.NotNull;

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
//            Intent intent = new Intent(mContext, ArticleActivity.class);
//            intent.putExtra("article", item);
//            startActivity(intent);
        });
        adapter.setOnItemLongClickListener((view, item) -> true);

        GroupModel addModel = new GroupModel();
        addModel.setId("add_group");
        addModel.setName("그룹 추가");

        GroupModel model = new GroupModel();
        model.setName("선린인터넷고등학교 분실물센터");
        model.setImage(getString(R.string.test_image));

        GroupModel model2 = new GroupModel();
        model2.setName("서울지하철 분실물센터");
        model2.setImage(getString(R.string.test_image));

        GroupModel model3 = new GroupModel();
        model3.setName("위워크 분실물센터");
        model3.setImage(getString(R.string.test_image));

        GroupModel model4 = new GroupModel();
        model4.setName("정신줄 관리센터");
        model4.setImage(getString(R.string.test_image));

        groupItems.add(addModel);
        groupItems.add(model);
        groupItems.add(model2);
        groupItems.add(model3);
        groupItems.add(model4);

        UserModel user = new UserModel();
        user.setName("정찬효");
        user.setEmail("roian6@naver.com");
        user.setProfile(getString(R.string.test_image));

        binding.setUser(user);

        return binding.getRoot();
    }

}
