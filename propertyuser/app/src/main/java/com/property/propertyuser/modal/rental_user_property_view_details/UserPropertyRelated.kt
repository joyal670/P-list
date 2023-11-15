package com.property.propertyuser.modal.rental_user_property_view_details

data class UserPropertyRelated(
    val id: Int,
    val property_name: String,
    val property_reg_no: String,
    val property_to: Int,
    val rent: String,
    val status: Int
)