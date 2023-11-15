package com.property.propertyuser.modal.desired_property_request_details

data class RequestDetails(
    val area: Int,
    val category: Int,
    val description: String,
    val id: Int,
    val max_price: String,
    val min_price: String,
    val property_to: Int,
    val property_type: PropertyType,
    val request_date: String,
    val request_id_no: String,
    val status: Int,
    val type_id: Int,
    val location:String
)