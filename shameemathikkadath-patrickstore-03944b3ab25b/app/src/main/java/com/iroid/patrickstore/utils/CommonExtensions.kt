package com.iroid.patrickstore.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import com.iroid.patrickstore.R
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ln
import kotlin.math.pow

fun TextView.leftDrawable(@DrawableRes id: Int = 0) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, id, 0, 0)
}

fun Int.dpToPx(displayMetrics: DisplayMetrics): Int = (this * displayMetrics.density).toInt()
//fun View.snack(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
//    Snackbar.make(this, message, duration)
//        .setTextColor(ContextCompat.getColor(context, R.color.white))
//        .setBackgroundTint(ContextCompat.getColor(context, R.color.green_solid))
//        .show()
//}

//fun Snackbar.setIcon(drawable: Drawable, @ColorInt colorTint: Int): Snackbar {
//    return this.apply {
//        setAction(" ") {}
//        val textView = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_action)
//        textView.text = ""
//
//        drawable.setTint(colorTint)
//        drawable.setTintMode(PorterDuff.Mode.SRC_ATOP)
//        textView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
//    }
//}

//fun View.snackWhite(message: String, duration: Int = Snackbar.LENGTH_LONG) {
//    Snackbar.make(this, message, duration)
//        .setTextColor(ContextCompat.getColor(context, R.color.black_light))
//        .setBackgroundTint(ContextCompat.getColor(context, R.color.white_smoke))
//        .show()
//}

fun Context.copyToClipboard(textToCopy: CharSequence) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("text", textToCopy)
    clipboard.setPrimaryClip(clip)
}

fun ImageView.loadImagesProfileWithGlideExt(url: String) {
    Glide.with(this)
        .load(url)
        .centerCrop()
        .error(R.drawable.ic_account_circle)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(R.drawable.ic_account_circle)
        .into(this)
}

//fun ImageView.loadImagesWithGlideExt(url: String) {
//    Glide.with(this)
//        .load(url)
//        .centerCrop()
//        .error(R.drawable.placeholder_1)
//        .diskCacheStrategy(DiskCacheStrategy.ALL)
//        .placeholder(R.drawable.ic_image_placeholder_new)
//        .into(this)
//}
//
//fun ImageView.loadImagesWithGlideExtNew(url: String) {
//    Glide.with(this)
//        .load(url)
//        .centerCrop()
//        .error(R.drawable.placeholder_1)
//        .diskCacheStrategy(DiskCacheStrategy.ALL)
//        .placeholder(R.drawable.ic_image_placeholder)
//        .into(this)
//}
//
//fun ImageView.loadImagesWithGlideExtFull(url: String) {
//    Glide.with(this)
//        .load(url)
//        .centerCrop()
//        .error(R.drawable.shape_placeholder)
//        .diskCacheStrategy(DiskCacheStrategy.ALL)
//        .into(this)
//}

fun Context.getCurrentDate(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy")
    return sdf.format(Date())
}

fun Context.getCurrentDateOtherFormat(): String {
    val sdf = SimpleDateFormat("dd-MM-yyyy")
    return sdf.format(Date())
}

fun Double.currencyCountWithSuffix(): String {
    val suffixChars = "KMBTPE"
    val formatter = DecimalFormat("###.#")
    formatter.roundingMode = RoundingMode.DOWN

    return if (this < 1000.0) formatter.format(this)
    else {
        val exp = (ln(this) / ln(1000.0)).toInt()
        formatter.format(this / 1000.0.pow(exp.toDouble())) + suffixChars[exp - 1]
    }
}

fun Context.getDateStringToAnotherFormat(dateS: String): String {
    val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val formatter = SimpleDateFormat("dd-MM-yyyy")
    return formatter.format(parser.parse(dateS))
}

fun View.setMargins(
    left: Int = this.marginLeft,
    top: Int = this.marginTop,
    right: Int = this.marginRight,
    bottom: Int = this.marginBottom
) {
    layoutParams = (layoutParams as ViewGroup.MarginLayoutParams).apply {
        setMargins(left, top, right, bottom)
    }
}

fun Context.getTimeAgo(dateString: String): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    var date: Date? = null
    try {
        date = sdf.parse(dateString)
        println(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    val calendar = Calendar.getInstance()
    calendar.time = date

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val currentCalendar = Calendar.getInstance()

    val currentYear = currentCalendar.get(Calendar.YEAR)
    val currentMonth = currentCalendar.get(Calendar.MONTH)
    val currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH)
    val currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY)
    val currentMinute = currentCalendar.get(Calendar.MINUTE)

    return if (year < currentYear) {
        val interval = currentYear - year
        if (interval == 1) "$interval year ago" else "$interval years ago"
    } else if (month < currentMonth) {
        val interval = currentMonth - month
        if (interval == 1) "$interval month ago" else "$interval months ago"
    } else if (day < currentDay) {
        val interval = currentDay - day
        if (interval == 1) "$interval day ago" else "$interval days ago"
    } else if (hour < currentHour) {
        val interval = currentHour - hour
        if (interval == 1) "$interval hour ago" else "$interval hours ago"
    } else if (minute < currentMinute) {
        val interval = currentMinute - minute
        if (interval == 1) "$interval minute ago" else "$interval minutes ago"
    } else {
        "a moment ago"
    }
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

fun RecyclerView.addOnScrolledToEndGrid(onScrolledToEnd: () -> Unit) {

    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {

        private val VISIBLE_THRESHOLD = 0

        private var loading = true
        private var previousTotal = 0

        override fun onScrollStateChanged(
            recyclerView: RecyclerView,
            newState: Int
        ) {

            with(layoutManager as GridLayoutManager) {

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

fun convertArabic(arabicStr: String): String? {
    val chArr = arabicStr.toCharArray()
    val sb = StringBuilder()
    for (ch in chArr) {
        if (Character.isDigit(ch)) {
            sb.append(Character.getNumericValue(ch))
        } else if (ch == '٫') {
            sb.append(".")
        } else {
            sb.append(ch)
        }
    }
    return sb.toString()
}
