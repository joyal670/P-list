package com.proteinium.proteiniumdietapp.pojo.subscption_info

data class Food(
    val average_rating: Float,
    val description: String,
    val id: Int,
    val image: String,
    val my_rating: Int,
    val name: String,
    val tags: List<Tag>,
    val tagline:String,
    val fat:Int,
    val calories:Int,
    val proteins:Int,
    val carbs:Int
)