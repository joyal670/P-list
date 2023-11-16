package com.ncomfortsagent.model.property

data class PropertyNewUserProperty(
    val appartments_count: Int,
    val building_apartments: List<PropertyNewBuildingApartment>,
    val feature_details: String,
    val id: Int,
    val is_builder: Int,
    val mrp: String,
    val occupied: Int,
    val occupied_count: Int,
    val property_name: String,
    val property_priority_image: PropertyNewPropertyPriorityImageX,
    val property_to: Int,
    val rent: String,
    val type_details: PropertyNewTypeDetails,
    val type_id: Int,
    val vacated_count: Int
)