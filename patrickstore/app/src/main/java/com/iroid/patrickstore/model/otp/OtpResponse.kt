package com.iroid.patrickstore.model.otp

data class OtpResponse(
    val `data`: OtpData,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)