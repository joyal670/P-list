package com.property.propertyagent.modal.owner.owner_request_service_details

data class OwnerService(
    val date: String,
    val description: String,
    val id: Int,
    val property_name: String,
    val service_id: Int,
    val service_related: ServiceRelated,
    val status: Int,
    val time: String,
    val estimate_file: String
)