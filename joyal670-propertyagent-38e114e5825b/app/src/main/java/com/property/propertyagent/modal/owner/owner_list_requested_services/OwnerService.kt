package com.property.propertyagent.modal.owner.owner_list_requested_services

data class OwnerService(
    val date: String,
    val id: Int,
    val property_name: String,
    val service_id: Int,
    val service_related: ServiceRelated,
    val status: Int,
    val time: String
)