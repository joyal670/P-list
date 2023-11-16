package com.iroid.patrickstore.model.order_summary

data class OrderSummaryResponse(
    val `data`: OrderSummaryData,
    val error: Boolean,
    val msg: String,
    val statusCode: Int,
    val total: Double
)