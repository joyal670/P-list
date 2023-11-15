package com.iroid.healthdomain.ui.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.iroid.healthdomain.R


class Loader(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_layout)
        setCancelable(false)
    }
}