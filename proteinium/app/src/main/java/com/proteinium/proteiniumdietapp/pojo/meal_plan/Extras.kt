package com.proteinium.proteiniumdietapp.pojo.meal_plan

import com.google.gson.JsonObject

data class Extras(
    val data: List<HashMap<String,String>>,
    val id:String,
    val name:String,
    val qty:Int,
    val price:String

)
