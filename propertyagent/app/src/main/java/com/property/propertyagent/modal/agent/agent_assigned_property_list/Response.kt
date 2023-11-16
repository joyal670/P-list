package com.property.propertyagent.modal.agent.agent_assigned_property_list

data class Response(
    val current_page: String,
    val total_page_count: Int,
    val user_properties: List<UserProperty>
)