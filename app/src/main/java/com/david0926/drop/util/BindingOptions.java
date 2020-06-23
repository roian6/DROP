package com.david0926.drop.util;

import android.view.View;

import androidx.databinding.BindingConversion;


public class BindingOptions {

    @BindingConversion
    public static int convertBooleanToVisibility(boolean visible) {
        return visible ? View.VISIBLE : View.GONE;
    }


}
