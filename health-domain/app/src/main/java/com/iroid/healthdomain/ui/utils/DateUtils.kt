package com.iroid.healthdomain.ui.utils

import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    @JvmStatic
    fun getDateStringToAnotherFormat(dateS : String) : String {
        val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val formatter = SimpleDateFormat("dd-MM-yyyy")
        return formatter.format(parser.parse(dateS))
    }

}