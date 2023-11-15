package com.proteinium.proteiniumdietapp.pojo.home

data class HomeResponse(
    val code: Int,
    val `data`: Home,
    val message: String,
    val status: Boolean,
    val blocked: Int
)