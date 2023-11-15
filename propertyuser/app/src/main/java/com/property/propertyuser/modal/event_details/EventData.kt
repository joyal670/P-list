package com.property.propertyuser.modal.event_details

data class EventData(
    val admin_events: AdminEvents,
    val admin_id: Int,
    val description: String,
    val event_date: String,
    val event_docs: List<EventDoc>,
    val id: Int,
    val latitude: String,
    val longitude: String,
    val name: String,
    val short_description: String
)