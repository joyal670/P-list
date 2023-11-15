package com.proteinium.proteiniumdietapp.pojo.notifications

data class NotificationsResponse(
    val code: Int,
    val `data`: NotificationData,
    val message: String,
    val status: Boolean
)

