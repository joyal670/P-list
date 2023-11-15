package com.property.propertyuser.modal.property_list

data class PropertyData(
    val current_page: String,
    val properties: List<Property>,
    val total_page_count: Int
)