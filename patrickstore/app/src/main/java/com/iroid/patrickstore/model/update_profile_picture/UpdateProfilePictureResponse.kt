package com.iroid.patrickstore.model.update_profile_picture

data class UpdateProfilePictureResponse(
    val `data`: UpdateProfilePictureData,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)