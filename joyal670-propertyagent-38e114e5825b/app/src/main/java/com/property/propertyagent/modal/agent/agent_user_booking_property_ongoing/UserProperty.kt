package com.property.propertyagent.modal.agent.agent_user_booking_property_ongoing

data class UserProperty(
    val id: Int,
    val property_details: PropertyDetails,
    val property_id: Int,
    val property_priority_image: PropertyPriorityImage,
    val user_id: Int,
    val user_rel: UserRel
)