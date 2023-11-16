package com.iroid.patrickstore.model.login

data class LoginResponse(
    val `data`: Login,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)