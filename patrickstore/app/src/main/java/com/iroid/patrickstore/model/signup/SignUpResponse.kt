package com.iroid.patrickstore.model.signup

data class SignUpResponse(
    val `data`: SignUp,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)