package com.david0926.drop;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.david0926.drop.Retrofit.DROPRetrofit;
import com.david0926.drop.Retrofit.DROPRetrofitService;
import com.david0926.drop.databinding.ActivityGroupNewBinding;
import com.david0926.drop.util.MimeTypeUtil;
import com.david0926.drop.util.TokenCache;

import java.io.File;

import gun0912.tedimagepicker.builder.TedImagePicker;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupNewActivity extends AppCompatActivity {

    private Uri profileUri;
    private ActivityGroupNewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group_new);
        binding.setName("");
        binding.setDescription("");

        //finish activity, when back button pressed
        binding.toolbarGroupNew.setNavigationOnClickListener(view -> finish());

        binding.imgGourpNewEditPhoto.setOnClickListener(view -> {
            TedImagePicker
                    .with(this)
                    .showTitle(false)
                    .startAnimation(R.anim.slide_up, R.anim.slide_up_before)
                    .finishAnimation(R.anim.slide_down_before, R.anim.slide_down)
                    .start(this::setGroupImage);
        });

        binding.btnGroupNew.setOnClickListener(view -> {

            if (binding.getName().isEmpty() || binding.getDescription().isEmpty())
                showErrorMsg("빈칸을 모두 채워주세요.");

            else if (profileUri == null)
                showErrorMsg("그룹 사진을 등록해 주세요.");

            else { //confirm success
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("그룹 생성").setMessage("그룹은 추후 삭제하거나 변경할 수 없으니, 내용이 올바른지 꼼꼼하게 확인해주세요!");
                builder.setPositiveButton("생성", (dialogInterface, i) ->
                        newGroup(binding.getName(), binding.getDescription(), profileUri));
                builder.setNegativeButton("취소", (dialogInterface, i) -> {}).show();
            }

        });
    }

    void newGroup(String name, String description, Uri photo) {
        Log.d("debug", "newGroup: " + name + ", " + description);

        DROPRetrofitService mRetrofitAPI = DROPRetrofit.getInstance(this).getDropService();

        File file;
        try {
            file = new File(photo.getPath());
        } catch (NullPointerException e) {
            showErrorMsg("그룹 사진을 등록해 주세요.");
            return;
        }
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part photobody = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
        RequestBody namebody =
                RequestBody.create(MediaType.parse("multipart/form-data"), name);
        RequestBody descriptionbody =
                RequestBody.create(MediaType.parse("multipart/form-data"), description);
        Call<ResponseBody> mCallResponse = mRetrofitAPI.CreateGroup(TokenCache.getToken(GroupNewActivity.this).getAccess(), namebody, descriptionbody, photobody);
        mCallResponse.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.body().string() == null) {
                        showErrorMsg("이미 존재하는 그룹입니다");
                        return;
                    }
                    finish();
                } catch (Exception e) {
                    showErrorMsg("이미 존재하는 그룹입니다.");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showErrorMsg("서버가 응답하지 않습니다.");
            }
        });
    }

    private void setGroupImage(Uri uri) {
        String mimeType = MimeTypeUtil.getMimeType(this, uri);
        if (mimeType == null || mimeType.equals("image/jpeg") || mimeType.equals("image/png")) {
            Glide.with(this).load(uri).into(binding.imgGroupNewImage);
            profileUri = uri;
        } else showErrorMsg("올바른 형식의 이미지를 업로드 해주세요. (jpeg, png)");
    }

    private void showErrorMsg(String msg) {
        binding.txtGroupNewError.setVisibility(View.VISIBLE);
        binding.txtGroupNewError.setText(msg);
        binding.txtGroupNewError.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
    }

}
