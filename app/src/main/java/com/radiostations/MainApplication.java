package com.radiostations;

import android.app.Application;
import android.content.Context;

public class MainApplication extends Application {

    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static void setContext(Context context) {
        mContext = context;
    }
}
