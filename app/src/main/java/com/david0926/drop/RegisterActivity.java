package com.david0926.drop;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.david0926.drop.Interface.RegisterModel;
import com.david0926.drop.Interface.RetrofitRegisterInterface;
import com.david0926.drop.databinding.ActivityRegisterBinding;
import com.david0926.drop.model.UserModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
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

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference = firebaseStorage.getReference();

    private String uri;

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        binding.setActivity(this);
        binding.setOnProgress(false);

        //finish activity, when back button pressed
        binding.toolbarRegi.setNavigationOnClickListener(view -> finish());

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

            startProgress();
            hideKeyboard(this);

            if (TextUtils.isEmpty(binding.getName()) || TextUtils.isEmpty(binding.getEmail())
                    || TextUtils.isEmpty(binding.getPw()) || TextUtils.isEmpty(binding.getPwcheck())) //empty field
                showErrorMsg("Please fill all required fields.");

            else if (!isValidEmail(binding.getEmail())) //invalid email
                showErrorMsg("Please enter a valid email address.");

            else if (!isValidPw(binding.getPw())) //invalid password
                showErrorMsg("Please enter a valid password. (6~24 letters, 0-9 + A-z)");

            else if (!binding.getPw().equals(binding.getPwcheck())) //password confirm failed
                showErrorMsg("Please enter same password in both fields.");

            else if (binding.imgRegiProfile.getDrawable() == null) //profile image not uploaded
                showErrorMsg("Please upload your profile image.");

            else { //confirm success

                System.out.println("레트로핏 진입");
//                createAccount(imageToByte(binding.imgRegiProfile.getDrawable()),
//                        binding.getName(), binding.getEmail(), binding.getPw());
                createAccount(uri,
                        binding.getName(), binding.getEmail(), binding.getPw());

            }

        });

    }

    private void createAccount(String profile, String name, String email, String pw) {


//
//        OnSuccessListener<Void> firestoreSuccessListener = aVoid -> {
//
//            //3. firebase storage (upload profile image)
//            storageReference
//                    .child("profile/" + email + ".png")
//                    .putBytes(profile)
//                    .addOnSuccessListener(snapshot -> finishSignUp())
//                    .addOnFailureListener(e -> showErrorMsg(e.getLocalizedMessage()));
//        };
//
//        OnSuccessListener<AuthResult> authSuccessListener = task -> {
//
//            //2. firestore (upload user information)
//            firebaseFirestore
//                    .collection("users")
//                    .document(email)
//                    .set(new UserModel(name, email, timeNow()))
//                    .addOnSuccessListener(firestoreSuccessListener)
//                    .addOnFailureListener(e -> showErrorMsg(e.getLocalizedMessage()));
//        };
//
//        //1. firebase auth (create user)
//        firebaseAuth
//                .createUserWithEmailAndPassword(email, pw)
//                .addOnSuccessListener(this, authSuccessListener)
//                .addOnFailureListener(this, e -> showErrorMsg(e.getLocalizedMessage()));
//
        Retrofit register = new Retrofit.Builder()
                .baseUrl("https://api.drop.hadmarine.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitRegisterInterface mRetrofitAPI = register.create(RetrofitRegisterInterface.class);

        File file = new File(profile);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part photo = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
        Map<String, RequestBody> m = new ArrayMap<>();

        m.put("userid", RequestBody.create(MediaType.parse("multipart/form-data"), email));
        m.put("name", RequestBody.create(MediaType.parse("multipart/form-data"), name));
        m.put("password", RequestBody.create(MediaType.parse("multipart/form-data"), pw));
        m.put("photo", requestFile);


        Call<ResponseBody> mCallResponse = mRetrofitAPI.CreateUser(m);
        System.out.println("-=== 레드로핏 실행 ===-");
        mCallResponse.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            System.out.println("성공.");
                            System.out.println(response.body().string());
                    finishSignUp();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    System.out.println("E R R O R");
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });


        //coroutine later
    }


    private void finishSignUp() {
//        sendBroadcast(new Intent("finish_signup"));

        binding.animatorRegi.showNext();
        binding.lottieRegiFinish.playAnimation();

        new Handler().postDelayed(() -> {
            finish();
        }, binding.lottieRegiFinish.getDuration() + 1000);
    }

    private void setProfileImage(Uri uri) {
        if (getMimeType(uri).equals("image/jpeg") || getMimeType(uri).equals("image/png")) {
            Glide.with(this).load(uri).into(binding.imgRegiProfile);
            this.uri = uri.getPath();
        } else showErrorMsg("Please upload valid profile image. (jpeg, png)");
    }

    public String getMimeType(Uri uri) {

        String mimeType;
        if (uri.getScheme() != null && uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = this.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    private byte[] imageToByte(Drawable drawable) {

        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 10, outputStream);

        return outputStream.toByteArray();
    }

    private void showErrorMsg(String msg) {
        finishProgress();
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

    private void startProgress() {
        binding.txtRegiSignup.setVisibility(View.GONE);
        binding.progressRegi.setVisibility(View.VISIBLE);
    }

    private void finishProgress() {
        binding.txtRegiSignup.setVisibility(View.VISIBLE);
        binding.progressRegi.setVisibility(View.GONE);
    }

    private void hideKeyboard(Activity activity) {
        View v = activity.getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    private String timeNow() {
        return new SimpleDateFormat("yyyy/MM/dd hh:mm aa", Locale.ENGLISH).format(new Date());
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_down_before, R.anim.slide_down);
    }
}
