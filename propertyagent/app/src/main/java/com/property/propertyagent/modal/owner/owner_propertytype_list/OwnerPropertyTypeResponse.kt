package com.property.propertyagent.modal.owner.owner_propertytype_list

data class OwnerPropertyTypeResponse(
    val response: List<OwnerPropertyType> ,
    val status: Int // 200
)