package com.david0926.drop;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.david0926.drop.adapter.ArticleAdapter;
import com.david0926.drop.databinding.ActivityGroupInfoBinding;
import com.david0926.drop.model.ArticleModel;
import com.david0926.drop.model.GroupModel;
import com.david0926.drop.util.LinearLayoutManagerWrapper;

public class GroupInfoActivity extends AppCompatActivity {

    private ObservableArrayList<ArticleModel> articleItems = new ObservableArrayList<>();

    private ActivityGroupInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group_info);

        //finish activity, when back button pressed
        binding.toolbarGroupInfo.setNavigationOnClickListener(view -> finish());

        Intent getIntent = getIntent();
        binding.setGroup((GroupModel) getIntent.getSerializableExtra("group"));

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
    }
}
