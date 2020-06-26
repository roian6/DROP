package com.david0926.drop.util;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.BindingConversion;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.david0926.drop.R;
import com.david0926.drop.adapter.ArticleAdapter;
import com.david0926.drop.adapter.SocialGroupAdapter;
import com.david0926.drop.model.ArticleModel;
import com.david0926.drop.model.GroupModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class BindingOptions {

    @BindingConversion
    public static int convertBooleanToVisibility(boolean visible) {
        return visible ? View.VISIBLE : View.GONE;
    }

    @BindingAdapter("imageLink")
    public static void setImageLink(ImageView view, String link) {
        if (link == null || link.isEmpty()) return;
        Glide.with(view).load(link).into(view);
    }

    @BindingAdapter("articleItem")
    public static void bindArticleItem(RecyclerView recyclerView, ObservableArrayList<ArticleModel> items) {
        ArticleAdapter adapter = (ArticleAdapter) recyclerView.getAdapter();
        if (adapter != null) adapter.setItem(items);
    }

    @BindingAdapter("socialGroupItem")
    public static void bindSocialGroupItem(RecyclerView recyclerView, ObservableArrayList<GroupModel> items) {
        SocialGroupAdapter adapter = (SocialGroupAdapter) recyclerView.getAdapter();
        if (adapter != null) adapter.setItem(items);
    }

    @BindingAdapter("isViewSelected")
    public static void setIsViewSelected(View view, Boolean selected) {
        if (selected == null) return;
        view.setBackgroundTintList(ContextCompat.getColorStateList(view.getContext(),
                selected ? R.color.colorPrimary : R.color.colorWhite));
    }

    @BindingAdapter("isTextSelected")
    public static void setIsTextSelected(TextView view, Boolean selected) {
        if (selected == null) return;
        view.setTextColor(view.getContext().getColor(selected ? R.color.colorWhite : R.color.colorPrimary));
    }

    @BindingAdapter("buttonEnabled")
    public static void setButtonEnabled(Button button, Boolean enabled) {
        button.setEnabled(enabled);
        button.setBackgroundTintList(ContextCompat.getColorStateList(button.getContext(),
                enabled ? R.color.colorPrimary : R.color.materialGray5));
    }

    @BindingAdapter("articleType")
    public static void bindArticleType(TextView view, String type) {
        if (type == null) return;
        if (type.equals("lost")) {
            view.setText("분실");
            view.setTextColor(view.getContext().getColor(R.color.colorPrimary));
            view.setBackground(view.getContext().getDrawable(R.drawable.round_box_radius));
        } else {
            view.setText("습득");
            view.setTextColor(view.getContext().getColor(R.color.materialGreen));
            view.setBackground(view.getContext().getDrawable(R.drawable.round_box_radius_green));
        }

    }

    @BindingAdapter("timeago")
    public static void setTimeAgo(TextView view, String time) {
        if (time == null) return;
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH);
        String ago;
        try {
            Date past = format.parse(time);
            Date now = new Date();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());

            if (seconds < 60) ago = seconds + "초";
            else if (minutes < 60) ago = minutes + "분";
            else if (hours < 24) ago = hours + "시간";
            else ago = days + "일";
            ago += " 전";

            view.setText(ago);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


}
