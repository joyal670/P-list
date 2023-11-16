package com.iroid.healthdomain.ui.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import coil.load

class CoilImageLoader : lv.chi.photopicker.loader.ImageLoader {

    override fun loadImage(context: Context, view: ImageView, uri: Uri) {
        view.load(uri) {

        }
    }
}