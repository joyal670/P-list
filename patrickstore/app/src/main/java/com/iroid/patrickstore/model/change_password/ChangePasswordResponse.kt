package com.iroid.patrickstore.model.change_password

data class ChangePasswordResponse(
    val `data`: Any,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)