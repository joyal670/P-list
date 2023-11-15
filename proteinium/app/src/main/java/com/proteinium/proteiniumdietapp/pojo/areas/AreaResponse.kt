package com.proteinium.proteiniumdietapp.pojo.areas

data class AreaResponse(
    val code: Int,
    val `data`: List<Area>,
    val message: String,
    val status: Boolean
)