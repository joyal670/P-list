package com.proteinium.proteiniumdietapp.pojo.meal_plan

data class SubscriptionPlan(
    val suspend: String,
    val duration:String ,
    val price:Any,
    var isChecked:Boolean,
    var non_stop_price:String,
    var enable_modification:String
)