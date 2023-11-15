package com.iroid.patrickstore.model.update_profile

data class ProfileUpdateResponse(
    val `data`: ProfileUpdateData,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)