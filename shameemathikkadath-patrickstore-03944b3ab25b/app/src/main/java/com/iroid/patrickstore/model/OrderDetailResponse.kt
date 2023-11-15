package com.iroid.patrickstore.model

data class OrderDetailResponse(
    val `data`: Data,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)