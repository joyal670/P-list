package com.property.propertyagent.modal.agent.agent_property_appointment_start_tour

data class Response(
    val agent_commission: AgentCommission,
    val agent_tour: AgentTour,
    val property_details: PropertyDetails,
    val user_details: UserDetails
)