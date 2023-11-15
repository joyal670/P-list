package com.ncomfortsagent.model.property

data class PropertyNewBuildingApartment(
    val id: Int,
    val occupied: Int,
    val property_priority_image: PropertyNewPropertyPriorityImage,
    val type_id: Int,
    val type_details: PropertyNewTypeDetails
)