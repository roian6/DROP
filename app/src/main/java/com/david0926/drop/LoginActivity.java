package com.david0926.drop;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.david0926.drop.Interface.LoginModel;
import com.david0926.drop.Interface.RegisterModel;
import com.david0926.drop.Interface.RetrofitRegisterInterface;
import com.david0926.drop.databinding.ActivityLoginBinding;
import com.david0926.drop.model.UserModel;
import com.david0926.drop.util.UserCache;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

import gun0912.tedkeyboardobserver.TedKeyboardObserver;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference = firebaseStorage.getReference();

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setActivity(this);
        binding.setOnProgress(false);

        //scroll to bottom when keyboard up
        new TedKeyboardObserver(this).listen(isShow -> {
            binding.scrollLogin.smoothScrollTo(0, binding.scrollLogin.getBottom());
        });

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }

        //sign in button clicked
        binding.btnLoginSignin.setOnClickListener(view -> {

            binding.setOnProgress(true);
            hideKeyboard(this);

            String id = binding.getId(), pw = binding.getPw();

            if (TextUtils.isEmpty(id) || TextUtils.isEmpty(pw)) //empty field
                showErrorMsg("Please fill all required fields.");
            else signIn(id, pw);

        });

        //sign up button clicked
        binding.btnLoginRegi.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            overridePendingTransition(R.anim.slide_up, R.anim.slide_up_before);
        });

        //finish when sign up success
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action != null && action.equals("finish_signup")) {
                    finish();
                }
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter("finish_signup"));
    }

    private void signIn(String id, String pw) {
//
//        OnSuccessListener<Uri> storageSuccessListener = task -> {
//
//            //4. firebase auth (sign in)
//            firebaseAuth
//                    .signInWithEmailAndPassword(id, pw)
//                    .addOnSuccessListener(authResult -> finishSignIn())
//                    .addOnFailureListener(e -> {
//                        String errorMsg = e.getLocalizedMessage();
//
//                        if (errorMsg.contains("password is invalid")) {
//                            showErrorMsg("Please enter a valid password.");
//                        } else showErrorMsg(e.getLocalizedMessage());
//                    });
//        };
//
//        OnCompleteListener<DocumentSnapshot> firestoreCompleteListener = task -> {
//
//            DocumentSnapshot document = task.getResult();
//            if (document != null && document.exists()) {
//
//                //3. firebase storage (profile image check)
//                storageReference
//                        .child("profile/" + id + ".png")
//                        .getDownloadUrl()
//                        .addOnSuccessListener(storageSuccessListener)
//                        .addOnFailureListener(e -> showErrorMsg(e.getLocalizedMessage()));
//                //showErrorMsg("Profile image does not exist."));
//
//            } else showErrorMsg("User data does not exist.");
//        };
//
//        OnCompleteListener<SignInMethodQueryResult> emailCheckCompleteListener = task -> {
//            if (task.isSuccessful() && task.getResult() != null) {
//                List<String> signInMethods = task.getResult().getSignInMethods();
//                if (signInMethods != null && !signInMethods.isEmpty()) {
//
//                    //2. firestore (user data check)
//                    firebaseFirestore
//                            .collection("users")
//                            .document(id)
//                            .get()
//                            .addOnCompleteListener(firestoreCompleteListener);
//
//                } else showErrorMsg("User account does not exist.");
//            } else showErrorMsg("Please enter a valid email address.");
//        };
//
//        //1. firebase auth (account exist check)
//        firebaseAuth
//                .fetchSignInMethodsForEmail(id)
//                .addOnCompleteListener(emailCheckCompleteListener);
//
//
//        //coroutine this code later...

        Retrofit register = new Retrofit.Builder()
                .baseUrl("https://api.drop.hadmarine.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitRegisterInterface mRetrofitAPI = register.create(RetrofitRegisterInterface.class);
        Call<ResponseBody> mCallResponse = mRetrofitAPI.Login(new LoginModel(id,pw));
        System.out.println("-=== 로그인 레드로핏 실행 ===-");
        mCallResponse.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    System.out.println("성공.");
                    String body = response.body().string();
                    System.out.println();
                    JSONObject responseObject = new JSONObject(body).getJSONObject("data").getJSONObject("user");
                    UserCache.setUser(LoginActivity.this, new UserModel(responseObject.getString("name"),responseObject.getString("userid"), ""));
                    finishSignIn();
                } catch(Exception e) {
                    showErrorMsg("아이디또는 비밀번호가 일치하지 않습니다.");
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
    }

    private void finishSignIn() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.slide_down_before, R.anim.slide_down);
        finish();
    }

    private void showErrorMsg(String msg) {
        binding.setOnProgress(false);
        binding.txtLoginError.setVisibility(View.VISIBLE);
        binding.txtLoginError.setText(msg);
        binding.txtLoginError.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
    }

    private void hideKeyboard(Activity activity) {
        View v = activity.getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}
