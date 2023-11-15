package com.iroid.patrickstore.model.order_detail

data class OrderDetailResponse(
    val `data`: OrderDetail,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)