package com.property.propertyagent.modal.agent.agent_notification_list

data class Data(
    val current_page: String,
    val notifications: List<Notification>,
    val total_page_count: Int
)