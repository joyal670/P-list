package com.iroid.patrickstore.utils

import kotlin.math.roundToInt

fun Double.discountPercentage(actualPrice:Double):String{
   val  discount = 100 * (actualPrice - this) / actualPrice
    return "${discount.roundToInt()}% OFF"

}