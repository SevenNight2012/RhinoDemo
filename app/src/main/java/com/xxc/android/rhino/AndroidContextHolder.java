package com.xxc.android.rhino;


import android.content.Context;

/**
 * android context holder
 */
public class AndroidContextHolder {

    /**
     * get context
     *
     * @return global context
     */
    public static Context getContext() {
        return MyApp.getInstance().getApplicationContext();
    }
}
