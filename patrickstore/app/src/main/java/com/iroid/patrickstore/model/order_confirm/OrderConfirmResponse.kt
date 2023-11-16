package com.iroid.patrickstore.model.order_confirm

data class OrderConfirmResponse(
    val `data`: OrderConfirm,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)
