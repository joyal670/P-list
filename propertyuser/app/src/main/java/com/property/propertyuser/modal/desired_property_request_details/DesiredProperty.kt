package com.property.propertyuser.modal.desired_property_request_details

data class DesiredProperty(
    val Bathroom: Int,
    val Beds: Int,
    val apartments: Int,
    val area: Int,
    val floors: Int,
    val id: Int,
    var is_favourite: Int,
    val is_featured: Int,
    val mrp: String,
    val property_name: String,
    val property_priority_image: PropertyPriorityImage,
    val property_reg_no: String,
    val property_to: Int,
    val rating: String,
    val rent: String,
    val selling_price: String,
    val latitude : String,
    val longitude : String,
    val property_type : Any
)