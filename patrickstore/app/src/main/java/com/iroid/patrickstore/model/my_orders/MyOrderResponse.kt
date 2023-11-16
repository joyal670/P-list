package com.iroid.patrickstore.model.my_orders

data class MyOrderResponse(
    val `data`: MyOrder,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)