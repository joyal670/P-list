package com.iroid.patrickstore.model.login

data class Login(
    val email: String,
    val firstName: String,
    val imageUrl: String,
    val jwtToken: String,
    val lastName: String,
    val mobile: String
)