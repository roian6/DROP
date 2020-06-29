package com.david0926.drop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;

import android.content.Intent;
import android.os.Bundle;

import com.david0926.drop.databinding.ActivityArticleUploadBinding;
import com.david0926.drop.util.UserCache;

public class ArticleUploadActivity extends AppCompatActivity {

    private ActivityArticleUploadBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_article_upload);

        //finish activity, when back button pressed
        binding.toolbarArticleUpload.setNavigationOnClickListener(view -> finish());

        Intent getIntent = getIntent();
        binding.setType(getIntent.getStringExtra("type"));

        //binding.setGroupList(UserCache.getUser(this).getGroup());
    }
}
