package com.property.propertyuser.modal.proceed_book_property_details

data class UserBookedProperty(
    val cancel_status: Int,
    val document_verified: Int,
    val due_date: String,
    val id: Int,
    val property_id: Int,
    val rent: String,
    val status: Int
)