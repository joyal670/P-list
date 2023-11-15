package com.proteinium.proteiniumdietapp.pojo.meal_plan

data class MealPlanResponse(
    val code: Int,
    val `data`: Plan,
    val message: String,
    val status: Boolean

)