package com.property.propertyuser.modal.events_list

data class EventData(
    val current_page: String,
    val events: List<Event>,
    val total_page_count: Int
)