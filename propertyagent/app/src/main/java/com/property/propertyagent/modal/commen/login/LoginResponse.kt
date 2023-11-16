package com.property.propertyagent.modal.commen.login

data class LoginResponse(
    val `data`: Login ,
    val response: String , // Logged in successfully
    val status: Int // 200
)