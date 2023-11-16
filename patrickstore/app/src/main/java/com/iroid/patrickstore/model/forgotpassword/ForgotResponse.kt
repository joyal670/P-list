package com.iroid.patrickstore.model.forgotpassword

data class ForgotResponse(
    val `data`: Forgot,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)