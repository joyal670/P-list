package com.proteinium.proteiniumdietapp.pojo.subscriptions_history

data class SubscriptionsHistoryResponse(
    val code: Int,
    val `data`: Subscription,
    val message: String,
    val status: Boolean
)