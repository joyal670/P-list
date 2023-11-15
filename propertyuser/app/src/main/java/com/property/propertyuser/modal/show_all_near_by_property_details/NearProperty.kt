package com.property.propertyuser.modal.show_all_near_by_property_details

data class NearProperty(
    val id: Int,
    val latitude: String,
    val longitude: String,
    val property_to: Int,
    val rent: String,
    val selling_price: String
)