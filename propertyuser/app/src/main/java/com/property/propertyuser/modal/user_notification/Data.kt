package com.property.propertyuser.modal.user_notification

data class Data(
    val current_page: String,
    val notifications: List<Notification>,
    val total_page_count: Int
)