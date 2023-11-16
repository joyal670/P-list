package com.property.propertyagent.modal.agent.agent_notification_list

data class Notification(
    val date: String,
    val id: Int,
    val notification_heading: String,
    val notification_text: String,
    val status: String
)