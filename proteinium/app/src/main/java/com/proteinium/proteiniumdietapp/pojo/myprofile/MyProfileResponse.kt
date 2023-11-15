package com.proteinium.proteiniumdietapp.pojo.myprofile

data class MyProfileResponse(
    val code: Int,
    val `data`: MyProfile,
    val message: String,
    val status: Boolean,
    val blocked: Int
)