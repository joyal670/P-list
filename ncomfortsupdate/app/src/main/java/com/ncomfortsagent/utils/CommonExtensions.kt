package com.ncomfortsagent.utils

import android.net.Uri
import android.text.format.DateFormat
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.Timestamp
import com.ncomfortsagent.R
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun getCurrentDate(timestamp: Timestamp): String {
    val milliseconds = timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
    val sdf = SimpleDateFormat("MM-yyyy HH:mm:ss")
    val netDate = Date(milliseconds)
    return sdf.format(netDate).toString()
}

fun getCurrentDateNew(time: String): String {
    return DateFormat.format("dd-MM-yyyy HH:mm:ss",time.toLong()).toString()
}
fun ImageView.loadImageToCoil(url:String){
    this.load(url){
        placeholder(R.drawable.ic_image_placeholder_new)
        error(R.drawable.ic_image_placeholder_new)
        crossfade(750)
        build()
    }
}

fun ImageView.loadImagesProfileWithGlideExt(url: String) {
    Glide.with(this)
        .load(url)
        .centerCrop()
        .error(R.drawable.ic_profile_user)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(R.drawable.ic_profile_user)
        .into(this)
}

fun ImageView.loadImagesPropertyWithGlideExt(url: String) {
    Glide.with(this)
        .load(url)
        .centerCrop()
        .error(R.drawable.no_image)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(R.drawable.no_image)
        .into(this)
}
fun ImageView.loadImagesWithGlideExt(url: String) {
    Glide.with(this)
        .load(url)
        .centerCrop()
        .error(R.drawable.ic_image_placeholder_new)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(R.drawable.ic_image_placeholder_new)
        .into(this)
}
fun RecyclerView.addOnScrolledToEnd(onScrolledToEnd: () -> Unit) {

    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {

        private val VISIBLE_THRESHOLD = 0

        private var loading = true
        private var previousTotal = 0

        override fun onScrollStateChanged(
            recyclerView: RecyclerView,
            newState: Int
        ) {

            with(layoutManager as LinearLayoutManager) {

                val visibleItemCount = childCount
                val totalItemCount = itemCount
                val firstVisibleItem = findFirstVisibleItemPosition()

                if (loading && totalItemCount > previousTotal) {

                    loading = false
                    previousTotal = totalItemCount
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_THRESHOLD)) {

                    onScrolledToEnd()
                    loading = true
                }
            }
        }
    })
}