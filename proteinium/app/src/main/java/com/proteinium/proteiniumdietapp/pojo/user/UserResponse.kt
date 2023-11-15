package com.proteinium.proteiniumdietapp.pojo.user

import com.proteinium.proteiniumdietapp.pojo.myprofile.MyProfile

data class UserResponse(
    val code: Int,
    val `data`: MyProfile,
    val message: String,
    val status: Boolean
)