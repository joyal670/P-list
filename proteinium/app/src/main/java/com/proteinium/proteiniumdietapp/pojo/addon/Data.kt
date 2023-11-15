package com.proteinium.proteiniumdietapp.pojo.addon

import com.proteinium.proteiniumdietapp.pojo.subscription_meal_plan_details.test.Extra

data class Data(
    val carbs: List<Carb>,
    val carbs_alter_status: Boolean,
    val current_carbs: Int,
    val current_proteins: Int,
    val proteins: List<Protein>,
    val protien_reduction_status: Boolean,
    val protien_reduction_message:String,
    val carbs_message:String,
    val extras: List<ExtrasAdd>,
    val duration:Int

    )
