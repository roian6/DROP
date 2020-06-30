package com.david0926.drop.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;

import com.allattentionhere.fabulousfilter.AAH_FabulousFragment;
import com.david0926.drop.ArticleUploadActivity;
import com.david0926.drop.R;
import com.david0926.drop.databinding.FragmentFabBinding;

public class FabFragment extends AAH_FabulousFragment {

    public static FabFragment newInstance() {
        return new FabFragment();
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        FragmentFabBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.fragment_fab, null, false);

        binding.btnFragfabLost.setOnClickListener(view -> binding.setSelected("lost"));
        binding.btnFragfabFound.setOnClickListener(view -> binding.setSelected("found"));

        binding.btnFragfabUpload.setOnClickListener(view -> {
            closeFilter("close");
            Intent intent = new Intent(getContext(), ArticleUploadActivity.class);
            intent.putExtra("type", binding.getSelected());
            startActivity(intent);
        });

        setAnimationDuration(400);
        setPeekHeight(250);
        setViewgroupStatic(binding.constraintFabBottom);
        setViewMain(binding.relativeFabContent);
        setMainContentView(binding.getRoot());
        super.setupDialog(dialog, style);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().sendBroadcast(new Intent("invalidate_fab"));
    }
}
