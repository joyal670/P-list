package com.proteinium.proteiniumdietapp.utils

import android.util.Log
import java.math.RoundingMode
import java.text.DecimalFormat

fun Double.toPriceRound(): String {
  val df = DecimalFormat("0.000")
  df.roundingMode = RoundingMode.CEILING
  val price=CommonUtils.replaceArabicNumbers(this.toString())
  Log.e("123455", "toPriceRound: ${CommonUtils.replaceArabicNumbers(this.toString()).toDouble()}" )
  val number =price.split(".")
  Log.e("#12345678", "toPriceRound: $number" )
  val priceBuilder= StringBuilder()

  priceBuilder.append(number[0])
  priceBuilder.append(".")
  if(number[1].toInt()==0){
    priceBuilder.append("000")
  }
  if(number[1].toInt() in 1..250){
    priceBuilder.append(250)
  }else if (number[1].toInt() in 251..500){
    priceBuilder.append(500)
  }
  else if (number[1].toInt() in 501..750){
    priceBuilder.append(750)
  }
  else if (number[1].toInt() in 750..999){
    val newPrice=number[0].toInt()+1
    priceBuilder.clear()
    priceBuilder.append(newPrice)
    priceBuilder.append(".")
    priceBuilder.append("000")
  }
  return  priceBuilder.toString()
}
