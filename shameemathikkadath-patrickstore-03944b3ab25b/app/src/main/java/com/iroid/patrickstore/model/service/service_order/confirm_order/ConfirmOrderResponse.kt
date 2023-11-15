package com.iroid.patrickstore.model.service.service_order.confirm_order

data class ConfirmOrderResponse(
    val `data`: ConfirmOrder,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)
