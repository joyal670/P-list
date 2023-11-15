package com.property.propertyuser.modal.list_requested_service_details

data class UserService(
    val date: String,
    val id: Int,
    val service_id: Int,
    val service_related: ServiceRelated,
    val status: Int,
    val time: String,
    var property_name : String,
    val service_amount: ServiceAmount
)