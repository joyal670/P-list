package com.proteinium.proteiniumdietapp.pojo.subscription_meal_plan_details

data class SubscriptionMealPlanDetailsResponse(
    val code: Int,
    val `data`: SubscriptionMealPlanDetail,
    val message: String,
    val status: Boolean
)