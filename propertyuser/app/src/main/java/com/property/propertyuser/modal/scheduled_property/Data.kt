package com.property.propertyuser.modal.scheduled_property

data class Data(
    val booked_tours: List<BookedTour>,
    val current_page: String,
    val total_page_count: Int
)