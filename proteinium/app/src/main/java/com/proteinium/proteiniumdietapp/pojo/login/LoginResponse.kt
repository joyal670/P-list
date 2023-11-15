package com.proteinium.proteiniumdietapp.pojo.login

data class LoginResponse(
    val code: Int,
    val message: String,
    val status: Boolean,
    val user: User,
    val plan_active_status:Boolean
)