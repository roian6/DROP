package com.david0926.drop;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;

public class LandingFragment extends Fragment implements ISlideBackgroundColorHolder {

    private static final String ARG_LAYOUT_RES_ID = "layoutResId";
    private int layoutResId;

    static LandingFragment newInstance(int layoutResId) {
        LandingFragment fragment = new LandingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LAYOUT_RES_ID, layoutResId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(ARG_LAYOUT_RES_ID)) {
            layoutResId = getArguments().getInt(ARG_LAYOUT_RES_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(layoutResId, container, false);
//        if (layoutResId == R.layout.activity_landing3) {
//
//        }
        return v;
    }

    @Override
    public int getDefaultBackgroundColor() {
//        Context context = getContext();
//        if(layoutResId==R.layout.activity_landing1&&context!=null)
//            return ContextCompat.getColor(context, R.color.materialDarkBlack);
//        else
        return Color.WHITE;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        View v = getView();
        if (v != null) v.setBackgroundColor(backgroundColor);
    }
}
