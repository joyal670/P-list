package com.proteinium.proteiniumdietapp.pojo.subscriptions_history

data class SubscriptionsHistory(
    val id: Int,
    val image: String,
    val meal_category_name: String,
    val meal_plan_name: String,
    val plan_status: String,
    val plan_start_date: String,
    val plan_end_date: String,
    val renew_status:String
)