package com.property.propertyuser.modal.signup

data class SignUpResponse(
    val otp: Int,
    val phone: String,
    val response: String,
    val status: Int
)