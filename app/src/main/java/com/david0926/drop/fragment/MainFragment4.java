package com.david0926.drop.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.david0926.drop.LoginActivity;
import com.david0926.drop.R;
import com.david0926.drop.databinding.DialogKeywordBinding;
import com.david0926.drop.databinding.FragmentMain4Binding;
import com.david0926.drop.util.UserCache;

import org.jetbrains.annotations.NotNull;

import java.util.List;

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
            List<String> oldKeywordList = UserCache.getUser(mContext).getKeyword();

            DialogKeywordBinding dialogKeywordBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
                    R.layout.dialog_keyword, null, false);
            dialogKeywordBinding.setKeyword(oldKeywordList != null ? TextUtils.join(",", oldKeywordList) : "");

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setView(dialogKeywordBinding.getRoot());

            AlertDialog dialog = builder.create();

            dialogKeywordBinding.btnKeywordConfirm.setOnClickListener(view1 -> {
                String[] keyword = dialogKeywordBinding.getKeyword().split(",");
                for (int i = 0; i < keyword.length; i++)
                    keyword[i] = keyword[i].trim();

                //gogo update user's keyword info with array 'keyword'
                //when finish, update UserCache with response UserModel
                //and do dialog.dismiss();
            });

            dialog.show();

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
                    .setPositiveButton("확인", (dialogInterface, i) -> {
                    })
                    .show();
        });

        return binding.getRoot();
    }

}
