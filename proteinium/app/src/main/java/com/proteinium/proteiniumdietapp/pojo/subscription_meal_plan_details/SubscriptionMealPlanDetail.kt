package com.proteinium.proteiniumdietapp.pojo.subscription_meal_plan_details



data class SubscriptionMealPlanDetail(
    val delivery_address: DeliveryAddress,
    val id: Int,
    val meal_category_name: String,
    val meal_plan_name: String,
    val payment_method: String,
    val promo_discount_price:Double,
    val plan_end_date: String,
    val plan_start_date: String,
    val plan_status: String,
    val total: Any,
    val percentage:Any,
    val meal_plan_id:Int,
    val enable_modifications:Int,
    val duration:Int,
    val non_stop_status:Int,
    val proteins:Int,
    val proteins_price:Int,
    val carbs:Int,
    val carbs_price:Int,
    val extras: List<Extra>,
    val extras_total: Int


)