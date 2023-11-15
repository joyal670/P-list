package com.proteinium.proteiniumdietapp.pojo.home



data class MealCategory(
    val id: Int,
    val image: String,
    val meal_plans: List<MealPlan>,
    val name: String
)