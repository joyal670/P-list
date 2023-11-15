package com.property.propertyagent.modal.agent.agent_user_booking_property_completed

data class UserProperty(
    val booked_date: String,
    val id: Int,
    val property_details: PropertyDetails,
    val property_id: Int,
    val property_priority_image: PropertyPriorityImage,
    val time_range: String,
    val user_id: Int,
    val user_rel: UserRel
)