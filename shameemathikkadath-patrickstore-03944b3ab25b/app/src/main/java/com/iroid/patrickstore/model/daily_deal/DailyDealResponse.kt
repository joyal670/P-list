package com.iroid.patrickstore.model.daily_deal

data class DailyDealResponse(
    val `data`: Data,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)
