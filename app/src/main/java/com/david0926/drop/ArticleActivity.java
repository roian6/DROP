package com.david0926.drop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.david0926.drop.databinding.ActivityArticleBinding;

public class ArticleActivity extends AppCompatActivity {

    private ActivityArticleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_article);

        //finish activity, when back button pressed
        binding.toolbarArticle.setNavigationOnClickListener(view -> finish());
    }
}
