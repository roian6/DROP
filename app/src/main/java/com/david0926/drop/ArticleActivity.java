package com.david0926.drop;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.david0926.drop.Retrofit.DROPRetrofit;
import com.david0926.drop.Retrofit.DROPRetrofitService;
import com.david0926.drop.databinding.ActivityArticleBinding;
import com.david0926.drop.fragment.MainFragment1;
import com.david0926.drop.model.ArticleModel;
import com.david0926.drop.util.TokenCache;
import com.david0926.drop.util.UserCache;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        if (getIntent().getBooleanExtra("to_comment", false)) showComment(false);

        binding.btnArticleComment.setOnClickListener(view -> showComment(false));
        binding.btnArticleImportant.setOnClickListener(view -> showComment(true));

        if (model.getReward().contains("Non-Reward")) { // 보상이 없다면 보상칸은 지워버립세!
            binding.txtArticleProductaddinfo.setVisibility(View.GONE);
            binding.txtArticleProductaddinfotitle.setVisibility(View.GONE);
        }
    }

    void showComment(boolean isImportant) {
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra("is_important", isImportant);
        intent.putExtra("article", model);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_article, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (model.getUser().get_id().equals(UserCache.getUser(this).get_id())) { //my article
            menu.findItem(R.id.action_article_resolve).setVisible(true);
            menu.findItem(R.id.action_article_edit).setVisible(true);
            menu.findItem(R.id.action_article_delete).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch (item.getItemId()) {
            case R.id.action_article_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, String.format(getString(R.string.share_text),
                        model.getTitle(), model.getDescription(), model.getTime(), model.getPlace()));
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                break;
            case R.id.action_article_resolve:
                builder.setTitle("해결 완료")
                        .setMessage("이 게시물을 해결 완료 처리할까요? 더 이상 피드에 노출되지 않습니다.")
                        .setPositiveButton("확인", (dialogInterface, i) -> {
                            DROPRetrofitService mRetrofitAPI = DROPRetrofit.getInstance(this).getDropService();
                            Call<ResponseBody> mCallResponse = mRetrofitAPI.setPostSolved(TokenCache.getToken(this).getAccess(), model.get_id());
                            mCallResponse.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    try {
                                        if (response.body() != null) {
                                            finish();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                }
                            });
                        })
                        .setNegativeButton("취소", (dialogInterface, i) -> {
                        }).show();
                break;

            case R.id.action_article_edit:
                Intent intent = new Intent(ArticleActivity.this, ArticleUploadActivity.class);
                intent.putExtra("is_edit", true);
                intent.putExtra("article", model);
                startActivity(intent);
                finish();
                break;

            case R.id.action_article_delete:
                builder.setTitle("게시물 삭제")
                        .setMessage("이 게시물을 삭제할까요? 삭제된 게시물은 복구할 수 없습니다.")
                        .setPositiveButton("확인", (dialogInterface, i) -> {
                            //gogo
                            DROPRetrofitService mRetrofitAPI = DROPRetrofit.getInstance(this).getDropService();

                            Call<ResponseBody> mCallResponse = mRetrofitAPI.DeletePost(TokenCache.getToken(this).getAccess(), model.get_id());
                            mCallResponse.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    try {
                                        String body = response.body().string();
                                        MainFragment1.isNeedInit = true;
                                        finish();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                }
                            });

                        })
                        .setNegativeButton("취소", (dialogInterface, i) -> {
                        }).show();
                break;
            default:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
