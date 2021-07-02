package com.example.xiaodongonline.components

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.example.xiaodongonline.R

class LoadingView(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_view)
        setCancelable(false)
    }
}