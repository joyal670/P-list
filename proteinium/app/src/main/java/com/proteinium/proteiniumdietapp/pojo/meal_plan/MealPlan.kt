package com.proteinium.proteiniumdietapp.pojo.meal_plan

data class MealPlan(
    val description: String,
    val id: Int,
    val image: String,
    val meal_category_id: Int,
    val meal_category_name: String,
    val meal_types: List<MealType>,
    val name: String,
    val price: Int,
    val durations: List<SubscriptionPlan>,
    val info_text: String,
    val off_days: List<String>,
    val plan_end_date:String

)