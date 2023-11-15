package com.proteinium.proteiniumdietapp.pojo.getNotification

data class GetNotificationResponse(
    val code: Int,
    val `data`: GetNotification,
    val message: String,
    val status: Boolean
)