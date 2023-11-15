package com.property.propertyuser.modal.show_all_near_by_property_details

data class MainProperty(
    val Bathroom: Int,
    val Beds: Int,
    val Restaurant: Int,
    val apartments: Int,
    val area: Int,
    val conference_hall: Int,
    val floors: Int,
    val id: Int,
    val latitude: String,
    val longitude: String,
    val property_name: String,
    val property_priority_image: PropertyPriorityImage,
    val property_to: Int,
    val property_type: String,
    val rating: String,
    val rent: String,
    val selling_price: String
)