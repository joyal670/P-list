package com.proteinium.proteiniumdietapp.pojo.payment

data class PaymentResponse(
    val Data: Payment,
    val IsSuccess: Boolean,
    val Message: String,
    val ValidationErrors: Any
)