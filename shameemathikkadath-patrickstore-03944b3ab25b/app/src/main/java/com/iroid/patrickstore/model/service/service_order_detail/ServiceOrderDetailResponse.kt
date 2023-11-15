package com.iroid.patrickstore.model.service.service_order_detail

data class ServiceOrderDetailResponse(
    val `data`: ServiceOrder,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)
