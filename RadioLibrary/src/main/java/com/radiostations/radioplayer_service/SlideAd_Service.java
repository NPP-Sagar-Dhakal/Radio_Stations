package com.radiostations.radioplayer_service;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SlideAd_Service {
    public static final String SLIDE_AD = "SLIDE_AD";

    public static void putInt(Context context, int value) {
        SharedPreferences m = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = m.edit();
        editor.putInt(SLIDE_AD, value);
        editor.apply();
    }
}
