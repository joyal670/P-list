package com.ncomfortsagent.model.building_details

data class AgentBuildingDetailsData(
    val appartments_count: Int,
    val building_apartments: List<AgentBuildingDetailsBuildingApartment>,
    val city: String,
    val city_rel: AgentBuildingDetailsCityRel,
    val documents: List<AgentBuildingDetailsDocument>,
    val id: Int,
    val is_builder: Int,
    val occupied_count: Int,
    val property_name: String,
    val property_reg_no: String,
    val property_to: Int,
    val type_id: Int,
    val vacated_count: Int,
    val type_details: AgentBuildingDetailsTypeDetails
)