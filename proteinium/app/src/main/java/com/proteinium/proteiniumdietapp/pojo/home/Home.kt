package com.proteinium.proteiniumdietapp.pojo.home

data class Home(
    val banners: List<Banner>,
    val notification_count:Int,
    val meal_categories: List<MealCategory>
)