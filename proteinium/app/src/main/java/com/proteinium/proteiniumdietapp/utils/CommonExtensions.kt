package com.proteinium.proteiniumdietapp.utils


import android.content.Context
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.proteinium.proteiniumdietapp.R
import java.lang.Math.abs
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

fun TextView.leftDrawable(@DrawableRes id: Int = 0) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, id, 0, 0)
}
fun Int.dpToPx(displayMetrics: DisplayMetrics): Int = (this * displayMetrics.density).toInt()

fun String.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

 fun isValidMobile(phone: String): Boolean {
     Log.e("123", "isValidMobile: "+phone.length )
    return if (!Pattern.matches("[a-zA-Z]+", phone)) {
        phone.length == 8
    } else false
}

fun Context.diffBetweenTwoStringDates(startDate:String,endDate:String):String{

    val date1: Date
    val date2: Date
    val dates = SimpleDateFormat("yyyy-MM-dd")
    date1 = dates.parse(startDate)
    date2 = dates.parse(endDate)
    val difference: Long = abs(date1.time - date2.time)
    val differenceDates = difference / (24 * 60 * 60 * 1000)
    val dayDifference = differenceDates.toString()
    return dayDifference
}
fun Context.getCurrentDate():String{
    val sdf = SimpleDateFormat("dd/MM/yyyy")
    return sdf.format(Date())
}
fun Context.getDateStringToAnotherFormat(dateS:String):String{
    val parser =  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    val formatter = SimpleDateFormat("dd/MM/yyyy")
    return formatter.format(parser.parse(dateS))
}
fun View.snack(message: String, duration: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(this, message, duration)
        .setTextColor(ContextCompat.getColor(context, R.color.white))
        .setBackgroundTint(ContextCompat.getColor(context, R.color.orange1))
        .show()
}