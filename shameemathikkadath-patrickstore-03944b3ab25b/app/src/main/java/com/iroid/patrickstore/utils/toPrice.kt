package com.iroid.patrickstore.utils

import java.text.DecimalFormat

fun Double.toPrice(): String {
  val pattern = "#,###.00"
  val decimalFormat = DecimalFormat(pattern)
  decimalFormat.groupingSize = 3

  return  decimalFormat.format(this)
}

fun String.toCovertOffer(offer: String):String{
    val diff=this.toDouble()-offer.toDouble()
    val offer=(diff/this.toDouble())*100
    return "${offer.toInt()}% OFF"
}
