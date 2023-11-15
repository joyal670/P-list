package com.property.propertyagent.modal.owner.owner_maintance_status

data class OwnerMaintanceStatusOwnerServiceData(
    val current_page: String ,
    val owner_services: List<OwnerMaintanceStatusOwnerService> ,
    val total_page_count: Int
)