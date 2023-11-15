package com.property.propertyagent.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import com.property.propertyagent.R
import java.text.SimpleDateFormat
import java.util.*

fun TextView.leftDrawable(@DrawableRes id : Int = 0) {
    this.setCompoundDrawablesWithIntrinsicBounds(0 , id , 0 , 0)
}

fun Int.dpToPx(displayMetrics : DisplayMetrics) : Int = (this * displayMetrics.density).toInt()
fun View.snack(message : String , duration : Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(this , message , duration)
        .setTextColor(ContextCompat.getColor(context , R.color.white))
        .setBackgroundTint(ContextCompat.getColor(context , R.color.green_solid))
        .show()
}

fun ImageView.loadImagesWithGlideExt(url : String) {
    Glide.with(this)
        .load(url)
        .centerCrop()
        .error(R.drawable.ic_broken_image_new)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(R.drawable.ic_image_placeholder_new)
        .into(this)
}

fun ImageView.loadImagesWithGlideExtByFitCenter(url : String) {
    Glide.with(this)
        .load(url)
        .fitCenter()
        .error(R.drawable.ic_broken_image_new)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(R.drawable.ic_image_placeholder_new)
        .into(this)
}

fun Context.getCurrentDate() : String {
    val sdf = SimpleDateFormat("dd-MM-yyyy")
    return sdf.format(Date())
}

fun Context.getDateStringToAnotherFormat(dateS : String) : String {
    val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val formatter = SimpleDateFormat("dd-MM-yyyy")
    return formatter.format(parser.parse(dateS))
}

fun View.setMargins(
    left : Int = this.marginLeft ,
    top : Int = this.marginTop ,
    right : Int = this.marginRight ,
    bottom : Int = this.marginBottom
) {
    layoutParams = (layoutParams as ViewGroup.MarginLayoutParams).apply {
        setMargins(left , top , right , bottom)
    }
}

fun Context.getDateFormatter(dateS : String) : String {
    val parser = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val formatter = SimpleDateFormat("MMM-yyyy", Locale.getDefault())
    return formatter.format(parser.parse(dateS))
}

fun RecyclerView.addOnScrolledToEnd(onScrolledToEnd : () -> Unit) {

    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {

        private val VISIBLE_THRESHOLD = 0

        private var loading = true
        private var previousTotal = 0

        override fun onScrollStateChanged(
            recyclerView : RecyclerView ,
            newState : Int
        ) {

            with(layoutManager as LinearLayoutManager) {

                val visibleItemCount = childCount
                val totalItemCount = itemCount
                val firstVisibleItem = findFirstVisibleItemPosition()

                if (loading && totalItemCount > previousTotal) {

                    loading = false
                    previousTotal = totalItemCount
                }
                /*  Log.e("totalitemcount",totalItemCount.toString())
                  Log.e("visibleItemCount",visibleItemCount.toString())
                  Log.e("firstVisibleItem",firstVisibleItem.toString())
                  Log.e("VISIBLE_THRESHOLD",VISIBLE_THRESHOLD.toString())
                  Log.e("end",(totalItemCount - visibleItemCount).toString()+"--"+(firstVisibleItem + VISIBLE_THRESHOLD).toString())*/
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_THRESHOLD)) {

                    onScrolledToEnd()
                    loading = true
                }
            }
        }
    })
}