package com.property.propertyuser.modal.my_property_list

data class BookedData(
    val booked_property: List<BookedProperty>,
    val current_page: String,
    val total_page_count: Int
)