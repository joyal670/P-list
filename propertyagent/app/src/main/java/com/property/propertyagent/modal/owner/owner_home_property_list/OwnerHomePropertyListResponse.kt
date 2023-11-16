package com.property.propertyagent.modal.owner.owner_home_property_list

data class OwnerHomePropertyListResponse(
    val properties: List<OwnerHomePropertyListProperty> ,
    val status: Int
)