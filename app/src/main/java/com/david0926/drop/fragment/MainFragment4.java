package com.david0926.drop.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.david0926.drop.LoginActivity;
import com.david0926.drop.R;
import com.david0926.drop.databinding.FragmentMain4Binding;
import com.david0926.drop.util.UserCache;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

import org.jetbrains.annotations.NotNull;

public class MainFragment4 extends Fragment {

    public static MainFragment4 newInstance() {
        return new MainFragment4();
    }

    private Context mContext;
    private FragmentMain4Binding binding;

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main4, container, false);

        //sorry for not using @string resource...

        binding.setUser(UserCache.getUser(mContext));

        binding.btnMain4EditProfile.setOnClickListener(view -> {

        });

        binding.btnMain4Logout.setOnClickListener(view -> {
            UserCache.logoutUser(mContext);
            startActivity(new Intent(mContext, LoginActivity.class));
            getActivity().finish();
        });

        binding.btnMain4Keyword.setOnClickListener(view -> {
            new AlertDialog.Builder(mContext)
                    .setTitle("준비중인 기능")
                    .setMessage("이 정도는 하루만에 만들 수 있죠! \n키워드 알림 기능을 기대해 주세요!")
                    .setPositiveButton("기대되네요!", (dialogInterface, i) -> {})
                    .show();
        });

//        binding.btnMain4Share.setOnClickListener(view -> {
//            Intent sendIntent = new Intent();
//            sendIntent.setAction(Intent.ACTION_SEND);
//            sendIntent.putExtra(Intent.EXTRA_TEXT, "편리한 분실물 공유 커뮤니티 서비스, DROP! \n" +
//                    "같이 사용해 보지 않을래요?\nhttps://github.com/roian6/DROP");
//            sendIntent.setType("text/plain");
//
//            Intent shareIntent = Intent.createChooser(sendIntent, null);
//            startActivity(shareIntent);
//        });

        binding.btnMain4Info.setOnClickListener(view -> {
            new AlertDialog.Builder(mContext)
                    .setTitle("개발자 정보")
                    .setMessage("DROP 1.0\n\nApp: 정찬효, 최종수\nBackend: 이호준\nDesign: 이단비")
                    .setPositiveButton("확인", (dialogInterface, i) -> {})
                    .show();
        });

        return binding.getRoot();
    }

}
