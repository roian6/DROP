package com.david0926.drop;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.david0926.drop.adapter.ArticleAdapter;
import com.david0926.drop.databinding.ActivityGroupInfoBinding;
import com.david0926.drop.model.ArticleModel;
import com.david0926.drop.model.GroupModel;
import com.david0926.drop.util.LinearLayoutManagerWrapper;
import com.david0926.drop.util.UserCache;

public class GroupInfoActivity extends AppCompatActivity {

    private ObservableArrayList<ArticleModel> articleItems = new ObservableArrayList<>();
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
            //해당 그룹 게시물 불러와서 articleItems에 넣어주세요
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (group.getUser_id() == null) return false;

        MenuInflater inflater = getMenuInflater();
        if (group.getUser_id().equals(UserCache.getUser(this).getId())) { //admin
            inflater.inflate(R.menu.menu_group_info_admin, menu);
        } else if (UserCache.getUser(this).getGroup().contains(group.getId())) { //member
            inflater.inflate(R.menu.menu_group_info_member, menu);
        }
        return true;
    }
}
