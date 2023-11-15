package com.property.propertyuser.modal.user_rewards

data class Data(
    val amount: String,
    val coupon_code: String,
    val coupon_type: String,
    val expiry_date: String,
    val id: Int,
    val percentage: String
)