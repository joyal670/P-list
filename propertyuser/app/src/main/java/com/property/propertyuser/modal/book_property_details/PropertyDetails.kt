package com.property.propertyuser.modal.book_property_details

data class PropertyDetails(
    val city: String,
    val id: Int,
    val location: String,
    val property_priority_image: PropertyPriorityImage,
    val property_reg_no: String,
    val property_name: String,
    val property_to: Int,
    val selling_price: String,
    val token_amount: String,
    val rent:String,
    val frequency:String
)