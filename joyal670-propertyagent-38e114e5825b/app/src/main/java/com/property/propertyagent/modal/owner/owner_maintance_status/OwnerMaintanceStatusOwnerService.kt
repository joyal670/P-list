package com.property.propertyagent.modal.owner.owner_maintance_status

data class OwnerMaintanceStatusOwnerService(
    val date: String ,
    val id: Int ,
    val property_name: String ,
    val service_id: Int ,
    val service_related: OwnerMaintanceStatusServiceRelated ,
    val status: Int ,
    val time: String
)