package com.proteinium.proteiniumdietapp.pojo.subscption_info

data class SubscriptionResponse(
    val code: Int,
    val `data`: Subscrption,
    val message: String,
    val status: Boolean,
    val blocked: Int
)