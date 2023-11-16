package com.property.propertyagent.modal.owner.owner_list_requested_services

data class OwnerServiceData(
    val current_page: String,
    val owner_services: List<OwnerService>,
    val total_page_count: Int
)