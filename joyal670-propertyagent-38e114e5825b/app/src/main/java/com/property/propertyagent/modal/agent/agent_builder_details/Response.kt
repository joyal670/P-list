package com.property.propertyagent.modal.agent.agent_builder_details

data class Response(
    val building_details: BuildingDetails,
    val units: List<Unit>
)