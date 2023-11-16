package com.property.propertyagent.modal.owner.owner_maintance

data class ServicesData(
    val current_page: String,
    val services: List<Service>,
    val total_page_count: Int
)