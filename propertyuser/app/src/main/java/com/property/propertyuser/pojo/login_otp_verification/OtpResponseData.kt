package com.property.propertyuser.pojo.login_otp_verification

data class OtpResponseData(
    val address: String,
    val api_token: String,
    val city: Int,
    val country: Int,
    val created_at: String,
    val email: String,
    val id: Int,
    val isOwner: Int,
    val latitude: Any,
    val longitude: Any,
    val name: String,
    val otp: Int,
    val phone: String,
    val profile_status: Int,
    val state: Int,
    val status: Int,
    val updated_at: String,
    val zip_code: String
)