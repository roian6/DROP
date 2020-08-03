package com.david0926.drop;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.david0926.drop.Retrofit.DROPRetrofit;
import com.david0926.drop.Retrofit.DROPRetrofitService;
import com.david0926.drop.adapter.CommentAdapter;
import com.david0926.drop.databinding.ActivityCommentBinding;
import com.david0926.drop.fragment.MainFragment1;
import com.david0926.drop.model.ArticleModel;
import com.david0926.drop.model.CommentModel;
import com.david0926.drop.util.LinearLayoutManagerWrapper;
import com.david0926.drop.util.TokenCache;
import com.david0926.drop.util.UserCache;
import com.google.gson.Gson;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity {

    private ObservableArrayList<CommentModel> commentItems = new ObservableArrayList<>();

    private ArticleModel article;
    private ActivityCommentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_comment);
        binding.setContent("");

        //finish activity, when back button pressed
        binding.toolbarComment.setNavigationOnClickListener(view -> finish());

        binding.setIsImportant(getIntent().getBooleanExtra("is_important", false));

        article = (ArticleModel)getIntent().getSerializableExtra("article");
        binding.setType(article.getType());

        LinearLayoutManagerWrapper wrapper = new LinearLayoutManagerWrapper(
                this, LinearLayoutManager.VERTICAL, false);
        binding.recyclerComment.setLayoutManager(wrapper);

        CommentAdapter adapter = new CommentAdapter();
        binding.recyclerComment.setAdapter(adapter);
        binding.setCommentList(commentItems);

        refreshComment();

        binding.btnComment.setOnClickListener(view -> {
            if(binding.getContent().isEmpty()) return;
            uploadComment(article.get_id(), binding.getContent(), binding.getIsImportant());
        });

        adapter.setOnItemClickListener((view, item) -> {

        });
        adapter.setOnItemLongClickListener((view, item) -> {
            if (!item.getUser().get_id().equals(UserCache.getUser(this).get_id())) return false;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("댓글 삭제하기")
                    .setMessage("이 댓글을 삭제할까요?")
                    .setPositiveButton("삭제", (dialogInterface, i) -> {
                        //여기에 댓글 삭제 구현
                        DROPRetrofitService mRetrofitAPI = DROPRetrofit.getInstance(this).getDropService();

                        CommentModel target = null;
                        for(CommentModel cm : commentItems) { // 길게 눌렀던 댓글의 인덱스 번호를 확인하기 위해 댓글리스트에서 해당 댓글 검색
                            if(cm.get_id().equals(item.get_id()))
                                target = cm;
                        }

                        Call<ResponseBody> mCallResponse = mRetrofitAPI.DeleteComment(TokenCache.getToken(this).getAccess(), article.get_id(), Integer.toString(commentItems.indexOf(target))); // 인덱스 번호 전송
                        mCallResponse.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                try {
                                    refreshComment();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    })
                    .setNegativeButton("취소", (dialogInterface, i) -> {});
            builder.show();

            return true;
        });

    }

    void uploadComment(String postId, String content, Boolean isImportant){
        //여기에 댓글 작성 구현
        DROPRetrofitService mRetrofitAPI = DROPRetrofit.getInstance(this).getDropService();

        if(content.trim().isEmpty())
            return;
        binding.edtCommentText.setText("");

        /* 키보드 내리는 코드 */
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.edtCommentText.getWindowToken(), 0);
        } catch(Exception e) {
            e.printStackTrace();
        }
        /* ========*/

        RequestBody postid_body = RequestBody.create(MediaType.parse("multipart/form-data"), postId);
        RequestBody content_body = RequestBody.create(MediaType.parse("multipart/form-data"), content);
        RequestBody isimportant_body = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(isImportant));

        Call<ResponseBody> mCallResponse = mRetrofitAPI.addComment(TokenCache.getToken(this).getAccess(), postid_body, content_body, isimportant_body);
        mCallResponse.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.body().string() == null) {
                        // 만약에 에러메세지 띄울 수 있으면 ㄱㄱ
                        return;
                    }
                    refreshComment();
                } catch(Exception e) {
                    // 만약에 에러메세지 띄울 수 있으면 ㄱㄱ
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // 만약에 에러메세지 띄울 수 있으면 ㄱㄱ
            }
        });
    }

    void refreshComment() {

        DROPRetrofitService mRetrofitAPI = DROPRetrofit.getInstance(this).getDropService();

        Call<ResponseBody> mCallResponse = mRetrofitAPI.getPost(TokenCache.getToken(this).getAccess(), article.get_id());
        System.out.println("A ID : " + article.get_id());
        mCallResponse.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String body = response.body().string();
                    JSONObject obj = new JSONObject(body).getJSONObject("data");
                    System.out.println("ABC : " + obj);
                    Gson gson = new Gson();

                    article = gson.fromJson(obj.toString(), ArticleModel.class);

                    commentItems.clear();
                    commentItems.addAll(article.getComment());
                } catch(Exception err) {
                    err.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // 만약에 에러메세지 띄울 수 있으면 ㄱㄱ
            }
        });

    }
}
