package com.iroid.healthdomain.data.model_class.user_update

data class UpdateUserResponse(
        val updateUserData: UpdateUserData,
        val message: String,
        val status: Int
)