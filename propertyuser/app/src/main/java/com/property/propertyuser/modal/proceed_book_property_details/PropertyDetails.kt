package com.property.propertyuser.modal.proceed_book_property_details

data class PropertyDetails(
    val city: String,
    val documents: List<Document>,
    val id: Int,
    val property_reg_no: String,
    val property_to: Int,
    val selling_price: String,
    val user_booked_property: UserBookedProperty
)