package com.proteinium.proteiniumdietapp.pojo.subscriptions_history

data class Subscription(
    val subscriptions: List<SubscriptionsHistory>,
    val renewals: List<SubscriptionsHistory>
)