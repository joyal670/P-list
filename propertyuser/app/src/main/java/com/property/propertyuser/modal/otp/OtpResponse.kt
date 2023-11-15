package com.property.propertyuser.modal.otp

data class OtpResponse(
    val `data`: OtpData,
    val response: String,
    val status: Int
)