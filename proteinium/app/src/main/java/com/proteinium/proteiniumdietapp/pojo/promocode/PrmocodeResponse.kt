package com.proteinium.proteiniumdietapp.pojo.promocode

data class PrmocodeResponse(
    val code: Int,
    val `data`: Promocode,
    val message: String,
    val status: Boolean
)