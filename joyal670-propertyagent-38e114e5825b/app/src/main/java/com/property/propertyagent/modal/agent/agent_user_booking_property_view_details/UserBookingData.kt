package com.property.propertyagent.modal.agent.agent_user_booking_property_view_details

data class UserBookingData(
    val approve_status: Int,
    val check_in: String,
    val check_out: String,
    val id: Int,
    val is_verified: Int
)