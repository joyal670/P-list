package com.ncomfortsagent.model.property_details

data class AgentPropertyDetailsAmenityCategory(
    val amenity_details: List<AgentPropertyDetailsAmenityDetail>,
    val id: Int,
    val name: String
)