package com.xxc.android.rhino;

import androidx.annotation.Nullable;

/**
 * js callback
 */
public interface JsResultListener {
    void onResult(@Nullable String jsonResult, @Nullable Exception error);
}
