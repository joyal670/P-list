package com.iroid.healthdomain.data.model_class.user_profile

data class UserModelResponse(
    val `data`: UserData,
    val message: String,
    val status: Int
)