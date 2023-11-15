package com.property.propertyuser.modal.user_notification

data class Notification(
    val date: String,
    val id: Int,
    val notification_heading: String,
    val notification_text: String,
    val status: String
)