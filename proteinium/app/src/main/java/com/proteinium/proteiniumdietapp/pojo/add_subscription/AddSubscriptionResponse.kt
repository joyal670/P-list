package com.proteinium.proteiniumdietapp.pojo.add_subscription

data class AddSubscriptionResponse(
    val code: Int,
    val `data`: AddSubscription,
    val message: String,
    val status: Boolean
)