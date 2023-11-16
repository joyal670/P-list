package com.iroid.patrickstore.model.cashback

data class CashbackResponse(
    val `data`: Data,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)
