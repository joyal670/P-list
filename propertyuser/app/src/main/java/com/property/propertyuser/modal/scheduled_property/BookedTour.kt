package com.property.propertyuser.modal.scheduled_property

data class BookedTour(
    val booked_date: String,
    val id: Int,
    val property_details: PropertyDetails,
    val property_id: Int,
    val property_priority_image: PropertyPriorityImage,
    val status: Int,
    val time_range: String,
    val user_id: Int
)