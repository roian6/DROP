package com.david0926.drop;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;

import com.bumptech.glide.Glide;
import com.david0926.drop.Retrofit.DROPRetrofit;
import com.david0926.drop.Retrofit.DROPRetrofitService;
import com.david0926.drop.databinding.ActivityArticleUploadBinding;
import com.david0926.drop.fragment.MainFragment1;
import com.david0926.drop.model.ArticleModel;
import com.david0926.drop.model.GroupModel;
import com.david0926.drop.util.MimeTypeUtil;
import com.david0926.drop.util.TokenCache;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

import gun0912.tedimagepicker.builder.TedImagePicker;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticleUploadActivity extends AppCompatActivity {

    private ObservableArrayList<GroupModel> groupList = new ObservableArrayList<>();
    private Uri imageUri;

    private ActivityArticleUploadBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_article_upload);

        binding.setIsEdit(getIntent().getBooleanExtra("is_edit", false));
        binding.setArticle(getArticleModel());

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
            if (binding.getArticle().getTitle().isEmpty() || binding.getArticle().getTime().isEmpty()
                    || binding.getArticle().getPlace().isEmpty() || binding.getArticle().getDescription().isEmpty())
                showErrorMsg("빈칸을 모두 채워주세요.");

            else if (imageUri == null && !binding.getIsEdit())
                showErrorMsg("물건 이미지를 등록해 주세요.");

            else { //confirm success
                if (!binding.getIsEdit())
                    uploadArticle(groupList.get(binding.spinnerArticleUploadGroup.getSelectedItemPosition()).get_id(),
                            binding.getArticle(), getIntent().getStringExtra("type"), imageUri);
                else if (imageUri != null)
                    editArticleWithImage(binding.getArticle(), imageUri);
                else editArticle(binding.getArticle());
            }
        });

        DROPRetrofitService mRetrofitAPI = DROPRetrofit.getInstance(this).getDropService();
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
                    for (int i = array.length() - 1; i >= 0; i--) { // 최신순
                        GroupModel model = gson.fromJson(array.getJSONObject(i).toString(), GroupModel.class);
                        groupList.add(model);
                    }

                } catch (Exception e) {
                    noGroupError();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private ArticleModel getArticleModel() {
        Intent getIntent = getIntent();
        ArticleModel articleModel = new ArticleModel();

        if (getIntent.getBooleanExtra("is_edit", false)) {
            articleModel = (ArticleModel) getIntent.getSerializableExtra("article");
        } else {
            articleModel.setTitle("");
            articleModel.setTime("");
            articleModel.setPlace("");
            articleModel.setReward("");
            articleModel.setDescription("");
        }

        return articleModel;
    }

    private void uploadArticle(String group, ArticleModel model, String type, Uri image) {

        DROPRetrofitService mRetrofitAPI = DROPRetrofit.getInstance(this).getDropService();

        File file;
        try {
            file = new File(image.getPath());
        } catch (NullPointerException e) {
            showErrorMsg("이미지를 넣어주세요");
            return;
        }
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part photo = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
        RequestBody titlebody = RequestBody.create(MediaType.parse("multipart/form-data"), model.getTitle());
        RequestBody descriptionbody = RequestBody.create(MediaType.parse("multipart/form-data"), model.getDescription());
        RequestBody typebody = RequestBody.create(MediaType.parse("multipart/form-data"), type);
        RequestBody timebody = RequestBody.create(MediaType.parse("multipart/form-data"), model.getTime());
        RequestBody placebody = RequestBody.create(MediaType.parse("multipart/form-data"), model.getPlace());
        RequestBody rewardbody = RequestBody.create(MediaType.parse("multipart/form-data"), model.getReward().isEmpty() == false ? model.getReward() : "Non-Reward");
        RequestBody groupid = RequestBody.create(MediaType.parse("multipart/form-data"), group);

        Call<ResponseBody> mCallResponse = mRetrofitAPI.CreatePost(TokenCache.getToken(this).getAccess(), titlebody, descriptionbody, typebody, timebody, placebody, rewardbody, groupid, photo);
        mCallResponse.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String body;
                    if ((body = response.body().string()) == null) {
                        showErrorMsg("게시물을 업로드할 수 없습니다.");
                        return;
                    }
                    MainFragment1.isNeedInit = true;
                    finish();
                } catch (Exception e) {
                    showErrorMsg("게시물을 업로드할 수 없습니다.");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showErrorMsg("서버가 응답하지 않습니다.");
            }
        });

    }

    private void editArticle(ArticleModel model) {
        //gogo
        DROPRetrofitService mRetrofitAPI = DROPRetrofit.getInstance(this).getDropService();


        RequestBody titlebody = RequestBody.create(MediaType.parse("multipart/form-data"), model.getTitle());
        RequestBody typebody = RequestBody.create(MediaType.parse("multipart/form-data"), getArticleModel().getType());
        RequestBody descriptionbody = RequestBody.create(MediaType.parse("multipart/form-data"), model.getDescription());
        RequestBody timebody = RequestBody.create(MediaType.parse("multipart/form-data"), model.getTime());
        RequestBody groupbody = RequestBody.create(MediaType.parse("multipart/form-data"), getArticleModel().getGroup().get_id());
        RequestBody placebody = RequestBody.create(MediaType.parse("multipart/form-data"), model.getPlace());
        RequestBody rewardbody = RequestBody.create(MediaType.parse("multipart/form-data"), model.getReward().isEmpty() == false ? model.getReward() : "Non-Reward");

        Call<ResponseBody> mCallResponse = mRetrofitAPI.UpdatePost(TokenCache.getToken(this).getAccess(),getArticleModel().get_id(), titlebody, descriptionbody, typebody, timebody, placebody, rewardbody, groupbody, null);
        mCallResponse.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String body;
                    if ((body = response.body().string()) == null) {
                        showErrorMsg("게시물을 수정할 수 없습니다.");
                        return;
                    }
                    MainFragment1.isNeedInit = true;
                    finish();
                } catch (Exception e) {
                    showErrorMsg("게시물을 수정할 수 없습니다.");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showErrorMsg("서버가 응답하지 않습니다.");
            }
        });
    }

    private void editArticleWithImage(ArticleModel model, Uri image) {
        //gogo
        DROPRetrofitService mRetrofitAPI = DROPRetrofit.getInstance(this).getDropService();

        File file;
        try {
            file = new File(image.getPath());
        } catch (NullPointerException e) {
            showErrorMsg("이미지를 넣어주세요");
            return;
        }

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part photo = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
        RequestBody titlebody = RequestBody.create(MediaType.parse("multipart/form-data"), model.getTitle());
        RequestBody typebody = RequestBody.create(MediaType.parse("multipart/form-data"), getArticleModel().getType());
        RequestBody descriptionbody = RequestBody.create(MediaType.parse("multipart/form-data"), model.getDescription());
        RequestBody timebody = RequestBody.create(MediaType.parse("multipart/form-data"), model.getTime());
        RequestBody groupbody = RequestBody.create(MediaType.parse("multipart/form-data"), getArticleModel().getGroup().get_id());
        RequestBody placebody = RequestBody.create(MediaType.parse("multipart/form-data"), model.getPlace());
        RequestBody rewardbody = RequestBody.create(MediaType.parse("multipart/form-data"), model.getReward().isEmpty() == false ? model.getReward() : "Non-Reward");

        Call<ResponseBody> mCallResponse = mRetrofitAPI.UpdatePost(TokenCache.getToken(this).getAccess(),getArticleModel().get_id(), titlebody, descriptionbody, typebody, timebody, placebody, rewardbody, groupbody, photo);
        mCallResponse.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String body;
                    if ((body = response.body().string()) == null) {
                        showErrorMsg("게시물을 수정할 수 없습니다.");
                        return;
                    }
                    MainFragment1.isNeedInit = true;
                    finish();
                } catch (Exception e) {
                    showErrorMsg("게시물을 수정할 수 없습니다.");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showErrorMsg("서버가 응답하지 않습니다.");
            }
        });
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

    private void noGroupError() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("게시물을 작성하기 전에, 최소 하나 이상의 그룹에 가입해 주세요.");
//        builder.show();
        Toast.makeText(this, "게시물을 작성하기 전에, 최소 하나 이상의 그룹에 가입해 주세요.", Toast.LENGTH_SHORT).show();
        finish();
    }
}
