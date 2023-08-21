package com.xxc.android.rhino;

import android.app.Application;

/**
 * application
 */
public class MyApp extends Application {

    private static MyApp sInstance;

    public static Application getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        JsThread.init();
    }
}
