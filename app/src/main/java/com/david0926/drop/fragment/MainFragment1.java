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

import com.allattentionhere.fabulousfilter.AAH_FabulousFragment;
import com.david0926.drop.ArticleActivity;
import com.david0926.drop.R;
import com.david0926.drop.adapter.ArticleAdapter;
import com.david0926.drop.databinding.FragmentMain1Binding;
import com.david0926.drop.model.ArticleModel;
import com.david0926.drop.model.CommentModel;
import com.david0926.drop.util.LinearLayoutManagerWrapper;

import org.jetbrains.annotations.NotNull;

public class MainFragment1 extends Fragment{

    public static MainFragment1 newInstance() {
        return new MainFragment1();
    }

    private ObservableArrayList<ArticleModel> articleItems = new ObservableArrayList<>();

    private Context mContext;
    private FragmentMain1Binding binding;

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
            startActivity(new Intent(mContext, ArticleActivity.class));
        });
        adapter.setOnItemLongClickListener((view, item) -> true);

        ArticleModel model = new ArticleModel();

        model.setUser_name("정찬효");
        model.setType("lost");
        model.setUpload_time("2020/06/25 11:36:00");
        model.setProduct_name("AirPods Pro");
        model.setProduct_time("6/20 오후 3시쯤");
        model.setProduct_place("2학년 6반 교실");

        ArticleModel model2 = new ArticleModel();

        model2.setUser_name("김선린");
        model2.setType("found");
        model2.setUpload_time("2020/06/25 14:36:00");
        model2.setProduct_name("티머니 교통카드");
        model2.setProduct_time("6/25 아침");
        model2.setProduct_place("컴 14실");

        articleItems.add(model);
        articleItems.add(model2);


        return binding.getRoot();
    }
}
