package com.property.propertyagent.modal.agent.agent_user_booking_property_view_details

data class Data(
    val agent_commission: AgentCommission,
    val agent_tour: AgentTour,
    val id: Int,
    val property_data: PropertyData,
    val property_id: Int,
    val property_priority_image: PropertyPriorityImage,
    val user_booking_data: UserBookingData,
    val user_id: Int,
    val user_rel: UserRel
)