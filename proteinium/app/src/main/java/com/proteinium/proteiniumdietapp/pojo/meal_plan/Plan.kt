package com.proteinium.proteiniumdietapp.pojo.meal_plan

data class Plan(
    val carbs: List<Carb>,
    val meal_plan: MealPlan,
    val proteins: List<Protein>,
    val nonstop_delivery_price:Int,
    val plan_start_buffer:Int,
    val non_stop_check_day:String,
    var start_date_range:Int,
    var current_subscription_id:Int,
    val extras: List<Extras>,
    val global_suspensions: List<String>


)
