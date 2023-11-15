package com.property.propertyuser.modal.favorite_list

data class PropertyData(
    val current_page: String,
    val faviourate_properties: List<FaviourateProperty>,
    val total_page_count: Int
)