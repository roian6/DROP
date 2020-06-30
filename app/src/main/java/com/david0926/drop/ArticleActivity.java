package com.david0926.drop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.david0926.drop.databinding.ActivityArticleBinding;
import com.david0926.drop.model.ArticleModel;

import org.w3c.dom.Comment;

public class ArticleActivity extends AppCompatActivity {

    private ActivityArticleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_article);

        //finish activity, when back button pressed
        binding.toolbarArticle.setNavigationOnClickListener(view -> finish());

        Intent getIntent = getIntent();
        ArticleModel model = (ArticleModel) getIntent.getSerializableExtra("article");
        binding.setItem(model);

        binding.btnArticleComment.setOnClickListener(view -> {
            Intent intent = new Intent(this, CommentActivity.class);
            intent.putExtra("is_important", false);
            intent.putExtra("article", model);
            startActivity(intent);
        });

        binding.btnArticleImportant.setOnClickListener(view -> {
            Intent intent = new Intent(this, CommentActivity.class);
            intent.putExtra("is_important", true);
            intent.putExtra("article", model);
            startActivity(intent);
        });
    }
}
