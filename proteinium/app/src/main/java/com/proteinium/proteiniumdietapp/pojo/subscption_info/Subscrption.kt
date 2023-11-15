package com.proteinium.proteiniumdietapp.pojo.subscption_info

data class Subscrption(
    val address: Any,
    val adress_id: Any,
    val base_price: Any,
    val comments: String,
    val created_at: String,
    val duration: Int,
    val order_statuses: OrderStatuses,
    val id: Int,
    val meal_category_id: Int,
    val meal_plan_id: Int,
    val non_stop_delivery_price: Any,
    val non_stop_delivery_status: Int,
    val payment_status: Int,
    val plan_end_date: String,
    val plan_start_date: String,
    val total: Any,
    val updated_at: String,
    val user_id: Int,
    val off_days: List<String>,
    val global_suspensions: List<String>,
    val upcoming_plan_start_date:String,
    val upcoming_status:Boolean,
    val active_plan_end_date:String,
    val freeze_status:Int,
    val freeze_date:Any


)
