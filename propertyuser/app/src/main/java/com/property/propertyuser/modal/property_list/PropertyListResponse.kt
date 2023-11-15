package com.property.propertyuser.modal.property_list


data class PropertyListResponse(
    val events: List<Event>,
    val property_data: PropertyData,
    val status: Int
)