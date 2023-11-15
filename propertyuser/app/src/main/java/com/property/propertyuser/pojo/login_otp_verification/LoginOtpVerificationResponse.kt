package com.property.propertyuser.pojo.login_otp_verification

data class LoginOtpVerificationResponse(
    val `data`: OtpResponseData,
    val response: String,
    val status: Int
)