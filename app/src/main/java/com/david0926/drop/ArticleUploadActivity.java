package com.david0926.drop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.david0926.drop.databinding.ActivityArticleUploadBinding;

public class ArticleUploadActivity extends AppCompatActivity {

    private ActivityArticleUploadBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_article_upload);
    }
}
