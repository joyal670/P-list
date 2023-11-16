package com.property.propertyagent.modal.agent.agent_owner_completed_list

data class TourData(
    val current_page: String,
    val total_page_count: Int,
    val user_properties: List<UserProperty>
)