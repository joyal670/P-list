package com.property.propertyuser.modal.service_list

data class ServiceData(
    val current_page: String,
    val services: List<Service>,
    val total_page_count: Int
)