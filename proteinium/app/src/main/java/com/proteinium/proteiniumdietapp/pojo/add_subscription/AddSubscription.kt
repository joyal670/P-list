package com.proteinium.proteiniumdietapp.pojo.add_subscription

data class AddSubscription(
    val meal_plan_subscription_id: Int,
    val plan_summary: PlanSummary,
    val unique_key:String,
    val renewal:Boolean
)