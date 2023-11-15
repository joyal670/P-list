package com.ncomfortsagent.model.building_details

data class AgentBuildingDetailsBuildingApartment(
    val frequency: Int,
    val id: Int,
    val occupied: Int,
    val property_name: String,
    val property_priority_image: PropertyPriorityImage,
    val property_reg_no: String,
    val property_to: Int,
    val type_details: TypeDetails,
    val type_id: Int
)