package com.iroid.patrickstore.model.coupon

data class CouponResponse(
    val `data`: Coupon,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)