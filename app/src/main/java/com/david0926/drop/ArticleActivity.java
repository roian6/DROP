package com.david0926.drop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.david0926.drop.databinding.ActivityArticleBinding;
import com.david0926.drop.model.ArticleModel;
import com.david0926.drop.util.UserCache;

import org.w3c.dom.Comment;

public class ArticleActivity extends AppCompatActivity {

    private ArticleModel model;
    private ActivityArticleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_article);

        //finish activity, when back button pressed
        binding.toolbarArticle.setNavigationOnClickListener(view -> finish());

        setSupportActionBar(binding.toolbarArticle);

        model = (ArticleModel) getIntent().getSerializableExtra("article");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_article, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (model.getUser().get_id().equals(UserCache.getUser(this).get_id())) { //my article
            menu.findItem(R.id.action_resolve).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.action_resolve){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("해결 완료")
                    .setMessage("이 게시물을 해결 완료 처리할까요? 더 이상 피드에 노출되지 않습니다.")
                    .setPositiveButton("확인", (dialogInterface, i) -> {
                        //여기에 resolve 구현
                    })
                    .setNegativeButton("취소", (dialogInterface, i) -> {});
            builder.show();
        }

        return super.onOptionsItemSelected(item);
    }
}
