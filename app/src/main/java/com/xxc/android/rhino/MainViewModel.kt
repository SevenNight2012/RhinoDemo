package com.xxc.android.rhino

import android.util.Log
import androidx.lifecycle.ViewModel

/**
 * main ViewModel
 */
class MainViewModel : ViewModel() {

    companion object {
        const val TAG = "MainViewModel"
    }

    fun invokeJs() {
        // args place hold
        val args = listOf("2023-08-21 10:00:00", 1)
        JsThread.getInstance().invokeJs("getXingdu", args) { json, error ->
            if (null != error) {
                Log.e(TAG, "invokeJs failed: ${error.message}")
            } else {
                Log.d(TAG, "invokeJs success: $json")
            }
        }
    }

}