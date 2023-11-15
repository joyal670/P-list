package com.property.propertyagent.modal.agent.agent_start_owner_tour_property_details

data class Response(
    val agent_tour: AgentTour,
    val owner_details: OwnerDetails,
    val property_details: PropertyDetails
)