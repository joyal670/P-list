package com.iroid.patrickstore.model.view_profile

data class ViewProfileResponse(
    val `data`: ViewProfileData,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)