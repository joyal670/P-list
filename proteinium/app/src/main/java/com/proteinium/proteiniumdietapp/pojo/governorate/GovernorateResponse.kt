package com.proteinium.proteiniumdietapp.pojo.governorate

data class GovernorateResponse(
    val code: Int,
    val `data`: List<Governorates>,
    val message: String,
    val status: Boolean
)