package com.david0926.drop;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.david0926.drop.Interface.DROPRetrofitInterface;
import com.david0926.drop.databinding.ActivityRegisterBinding;
import com.david0926.drop.util.MimeTypeUtil;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gun0912.tedimagepicker.builder.TedImagePicker;
import gun0912.tedkeyboardobserver.TedKeyboardObserver;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RegisterActivity extends AppCompatActivity {

    private Uri uri;
    private String fcmToken = "";

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        binding.setActivity(this);
        binding.setOnProgress(false);

        //finish activity, when back button pressed
        binding.toolbarRegi.setNavigationOnClickListener(view -> finish());

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(runnable -> {
            fcmToken = runnable.getToken();
            System.out.println("fcm token: " + fcmToken);
        });

        //scroll to bottom when keyboard up
        new TedKeyboardObserver(this).listen(isShow -> {
            binding.scrollRegi.smoothScrollTo(0, binding.scrollRegi.getBottom());
        });

        //profile edit button clicked
        binding.imgRegiEditprofile.setOnClickListener(view -> {

            //start image picker
            TedImagePicker
                    .with(this)
                    .showTitle(false)
                    .startAnimation(R.anim.slide_up, R.anim.slide_up_before)
                    .finishAnimation(R.anim.slide_down_before, R.anim.slide_down)
                    .start(this::setProfileImage);

        });


        //sign up button clicked
        binding.btnRegiSignup.setOnClickListener(view -> {

            binding.setOnProgress(true);
            hideKeyboard(this);

            if (TextUtils.isEmpty(binding.getName()) || TextUtils.isEmpty(binding.getEmail())
                    || TextUtils.isEmpty(binding.getPw()) || TextUtils.isEmpty(binding.getPwcheck())) //empty field
                showErrorMsg("빈칸을 모두 채워주세요.");

            else if (!isValidEmail(binding.getEmail())) //invalid email
                showErrorMsg("올바른 이메일 주소를 입력해주세요.");

            else if (!isValidPw(binding.getPw())) //invalid password
                showErrorMsg("올바른 패스워드를 입력해주세요. (6~24자, 숫자+대소문자)");

            else if (!binding.getPw().equals(binding.getPwcheck())) //password confirm failed
                showErrorMsg("비밀번호가 일치하지 않습니다.");

            else if (binding.imgRegiProfile.getDrawable() == null) //profile image not uploaded
                showErrorMsg("프로필 사진을 넣어주세요.");

            else { //confirm success
                createAccount(uri, binding.getName(), binding.getEmail(), binding.getPw());
            }
        });

    }

    private void createAccount(Uri profile, String name, String email, String pw) {

        Retrofit register = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DROPRetrofitInterface mRetrofitAPI = register.create(DROPRetrofitInterface.class);

        File file;
        try {
            file = new File(profile.getPath());
        } catch (NullPointerException e) {
            showErrorMsg("프로필 사진을 넣어주세요");
            return;
        }
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part photo = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
        RequestBody userid =
                RequestBody.create(MediaType.parse("multipart/form-data"), email);
        RequestBody namebody =
                RequestBody.create(MediaType.parse("multipart/form-data"), name);

        RequestBody password =
                RequestBody.create(MediaType.parse("multipart/form-data"), pw);

        RequestBody fcmtoken =
                RequestBody.create(MediaType.parse("multipart/form-data"), fcmToken);

        Call<ResponseBody> mCallResponse = mRetrofitAPI.CreateUser(userid, password, namebody, fcmtoken, photo);
        mCallResponse.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.body().string() == null) {
                        showErrorMsg("이미 존재하는 계정입니다.");
                        return;
                    }
                    finishSignUp();
                } catch (Exception e) {
                    showErrorMsg("이미 존재하는 계정입니다.");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showErrorMsg("서버가 응답하지 않습니다.");
            }
        });

    }


    private void finishSignUp() {

        binding.animatorRegi.showNext();
        binding.lottieRegiFinish.playAnimation();

        new Handler().postDelayed(() -> {
            finish();
        }, binding.lottieRegiFinish.getDuration() + 1000);
    }

    private void setProfileImage(Uri uri) {
        String mimeType = MimeTypeUtil.getMimeType(this, uri);
        if (mimeType.equals("image/jpeg") || mimeType.equals("image/png")) {
            Glide.with(this).load(uri).into(binding.imgRegiProfile);
            this.uri = uri;
        } else showErrorMsg("올바른 형식의 이미지를 업로드 해주세요. (jpeg, png)");
    }

    private void showErrorMsg(String msg) {
        binding.setOnProgress(false);
        binding.txtRegiError.setVisibility(View.VISIBLE);
        binding.txtRegiError.setText(msg);
        binding.txtRegiError.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
    }

    private boolean isValidEmail(String target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private boolean isValidPw(String target) {
        //6~24 letters, 0~9 + A-z
        Pattern p = Pattern.compile("(^.*(?=.{6,24})(?=.*[0-9])(?=.*[A-z]).*$)");
        Matcher m = p.matcher(target);
        //except korean letters
        return m.find() && !target.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*");
    }

    private void hideKeyboard(Activity activity) {
        View v = activity.getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_down_before, R.anim.slide_down);
    }
}
