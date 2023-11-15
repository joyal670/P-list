package com.proteinium.proteiniumdietapp.utils

import android.util.Log
import java.text.DecimalFormat

fun Double.toPrice(): String {
  val df = DecimalFormat("0.000")
//  df.roundingMode = RoundingMode.CEILING
  val amount=df.format(this)
  Log.e("123456",amount)
  return  CommonUtils.replaceArabicNumbers(amount)
}
