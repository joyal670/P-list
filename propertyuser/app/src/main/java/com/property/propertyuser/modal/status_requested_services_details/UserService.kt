package com.property.propertyuser.modal.status_requested_services_details

data class UserService(
    val date: String,
    val description: String,
    val property_name:String,
    val id: Int,
    val service_id: Int,
    val service_related: ServiceRelated,
    val status: Int,
    val time: String,
    val estimate_file: String
)