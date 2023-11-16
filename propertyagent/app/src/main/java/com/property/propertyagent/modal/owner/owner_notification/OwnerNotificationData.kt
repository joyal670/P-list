package com.property.propertyagent.modal.owner.owner_notification

data class OwnerNotificationData(
    val current_page: String ,
    val notifications: List<OwnerNotificationNotification> ,
    val total_page_count: Int
)