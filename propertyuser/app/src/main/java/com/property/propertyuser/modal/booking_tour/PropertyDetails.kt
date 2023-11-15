package com.property.propertyuser.modal.booking_tour


data class PropertyDetails(
    val id: Int,
    val latitude: String,
    val longitude: String,
    val property_name:String,
    val property_reg_no: String,
    val property_priority_image: PropertyPriorityImage,
    val property_to: Int,
    val selling_price: String,
    val location:String,
    val rent:String
)