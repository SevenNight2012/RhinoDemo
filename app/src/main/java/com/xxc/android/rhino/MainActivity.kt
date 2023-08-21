package com.xxc.android.rhino

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private val mainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    private val btn by lazy {
        findViewById<TextView>(R.id.btn_invoke)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn.setOnClickListener {
            mainViewModel.invokeJs()
        }
    }
}