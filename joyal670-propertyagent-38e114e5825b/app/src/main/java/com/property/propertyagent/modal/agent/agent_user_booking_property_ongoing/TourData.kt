package com.property.propertyagent.modal.agent.agent_user_booking_property_ongoing

data class TourData(
    val current_page: String,
    val total_page_count: Int,
    val user_properties: List<UserProperty>
)