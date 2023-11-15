package com.iroid.emergency.start_up.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.iroid.emergency.R


class Loader(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.progress_dialog)
        setCancelable(false)
    }
}
