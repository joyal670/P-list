package com.proteinium.proteiniumdietapp.pojo.menu_day


import kotlin.collections.HashMap

data class Menu(
    val extras: List<Extra>,
    val disliked_tags: List<DislikedTag>,
    val meal_plan_limits: List<MealPlanLimit>,
    val meal_types: List<MealType>,
    val meal_type_id_combinations: HashMap<Int,Int>,
    val proteins:Int,
    val carbs:Int,
    val plan_carb:String

)
