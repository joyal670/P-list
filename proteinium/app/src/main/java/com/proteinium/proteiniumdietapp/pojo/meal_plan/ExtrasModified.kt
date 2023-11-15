package com.proteinium.proteiniumdietapp.pojo.meal_plan

import com.google.gson.JsonObject
import com.proteinium.proteiniumdietapp.pojo.subscription_meal_plan_details.ExtraModify

data class ExtrasModified(
    val id:String,
    val name:String,
    val extras_modify:List<ExtraModify>
)
