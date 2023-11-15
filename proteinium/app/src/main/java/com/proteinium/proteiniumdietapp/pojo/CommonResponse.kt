package com.proteinium.proteiniumdietapp.pojo


import com.proteinium.proteiniumdietapp.pojo.user.UserInfo

data class CommonResponse(
    val code: Int,
    val message: String,
    val status: Boolean,
    val access_token:String,
    val user: UserInfo,
    val promocode_discount_price:Double
)

