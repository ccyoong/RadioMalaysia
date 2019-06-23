package com.ccyoong.radiomalaysia.application;

import android.app.Application;
import android.content.Context;

import timber.log.Timber;

public class MyApplication extends Application {


    public static Context context;


    public static Context getAppContext() {
        return MyApplication.context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        MyApplication.context = getApplicationContext();
    }
}
