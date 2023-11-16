package com.iroid.patrickstore.model.coupon.applyCoupon

data class ApplyCouponResponse(
    val `data`: Data,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)
