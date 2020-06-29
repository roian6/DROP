package com.david0926.drop;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;

import com.bumptech.glide.Glide;
import com.david0926.drop.Interface.DROPRetrofitInterface;
import com.david0926.drop.databinding.ActivityArticleUploadBinding;
import com.david0926.drop.model.ArticleModel;
import com.david0926.drop.model.GroupModel;
import com.david0926.drop.util.MimeTypeUtil;
import com.david0926.drop.util.TokenCache;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import gun0912.tedimagepicker.builder.TedImagePicker;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArticleUploadActivity extends AppCompatActivity {

    private ObservableArrayList<GroupModel> groupList = new ObservableArrayList<>();
    private Uri imageUri;

    private ActivityArticleUploadBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_article_upload);

        ArticleModel articleModel = new ArticleModel();
        articleModel.setProduct_name("");
        articleModel.setProduct_time("");
        articleModel.setProduct_place("");
        articleModel.setProduct_addinfo("");
        articleModel.setProduct_desc("");

        binding.setArticle(articleModel);

        //finish activity, when back button pressed
        binding.toolbarArticleUpload.setNavigationOnClickListener(view -> finish());

        binding.setType(getIntent().getStringExtra("type"));
        binding.setGroupList(groupList);

        binding.imgArticleUploadImage.setOnClickListener(view -> {
            TedImagePicker
                    .with(this)
                    .showTitle(false)
                    .startAnimation(R.anim.slide_up, R.anim.slide_up_before)
                    .finishAnimation(R.anim.slide_down_before, R.anim.slide_down)
                    .start(this::setArticleImage);
        });

        binding.btnArticleUpload.setOnClickListener(view -> {
            if (binding.getArticle().getProduct_name().isEmpty() || binding.getArticle().getProduct_time().isEmpty()
                    || binding.getArticle().getProduct_place().isEmpty() || binding.getArticle().getProduct_addinfo().isEmpty()
                    || binding.getArticle().getProduct_desc().isEmpty())
                showErrorMsg("빈칸을 모두 채워주세요.");

            else if (imageUri == null)
                showErrorMsg("물건 이미지를 등록해 주세요.");

            else { //confirm success
                uploadArticle(groupList.get(binding.spinnerArticleUploadGroup.getSelectedItemPosition()).get_id(),
                        binding.getArticle(), getIntent().getStringExtra("type"), imageUri);
            }
        });

        Retrofit register = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DROPRetrofitInterface mRetrofitAPI = register.create(DROPRetrofitInterface.class);
        System.out.println("T " + TokenCache.getToken(this).getAccess());
        Call<ResponseBody> mCallResponse = mRetrofitAPI.MyGroups(TokenCache.getToken(this).getAccess());
        mCallResponse.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String body = response.body().string();
                    System.out.println(body);
                    JSONObject object = new JSONObject(body);
                    JSONArray array = object.getJSONArray("data");

                    Gson gson = new Gson();
                    for(int i = array.length()-1; i >= 0; i--) { // 최신순
                        GroupModel model = gson.fromJson(array.getJSONObject(i).toString(), GroupModel.class);
                        groupList.add(model);
                    }
                    if (groupList.isEmpty()) noGroupError();

                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    void uploadArticle(String group, ArticleModel model, String type, Uri image) {
        //gogo🥕
    }

    private void setArticleImage(Uri uri) {
        String mimeType = MimeTypeUtil.getMimeType(this, uri);
        if (mimeType == null || mimeType.equals("image/jpeg") || mimeType.equals("image/png")) {
            Glide.with(this).load(uri).into(binding.imgArticleUploadImage);
            imageUri = uri;
        } else showErrorMsg("올바른 형식의 이미지를 업로드 해주세요. (jpeg, png)");
    }

    private void showErrorMsg(String msg) {
        binding.txtArticleUploadError.setVisibility(View.VISIBLE);
        binding.txtArticleUploadError.setText(msg);
        binding.txtArticleUploadError.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
    }

    private void noGroupError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("게시물을 작성하기 전에, 최소 하나 이상의 그룹에 가입해 주세요.");
        builder.show();
        finish();
    }
}
