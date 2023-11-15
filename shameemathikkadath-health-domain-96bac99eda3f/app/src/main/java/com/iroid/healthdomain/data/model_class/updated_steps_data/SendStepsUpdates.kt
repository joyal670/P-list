package com.iroid.healthdomain.data.model_class.updated_steps_data

data class SendStepsUpdates(
    val activity_id: Int,
    val calories: Int,
    val date: String,
    val duration: Int,
    val steps: String
)