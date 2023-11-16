package com.iroid.patrickstore.model.service.service_order

data class ServiceOrderResponse(
    val `data`: ServiceOrder,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)
