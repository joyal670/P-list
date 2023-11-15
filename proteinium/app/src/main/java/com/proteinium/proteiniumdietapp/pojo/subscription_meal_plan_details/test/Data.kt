package com.proteinium.proteiniumdietapp.pojo.subscription_meal_plan_details.test

data class Data(
    val carbs: List<Carb>,
    val carbs_alter_status: Boolean,
    val carbs_message: String,
    val current_carbs: Int,
    val current_proteins: Int,
    val duration: Int,
    val extras: List<Extra>,
    val proteins: List<Protein>,
    val protien_reduction_message: String,
    val protien_reduction_status: Boolean
)
