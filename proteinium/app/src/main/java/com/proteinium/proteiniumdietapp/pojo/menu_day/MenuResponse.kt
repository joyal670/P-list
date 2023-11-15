package com.proteinium.proteiniumdietapp.pojo.menu_day

data class MenuResponse(
    val code: Int,
    val `data`: Menu,
    val message: String,
    val status: Boolean
)