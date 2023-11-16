package com.iroid.patrickstore.model.coupon.applyCoupon

data class Data(
    val extraAmount: Int,
    val grandTotal: Double,
    val isOfferValid: Boolean,
    val offerAmount: Double
)
