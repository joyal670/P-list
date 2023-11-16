package com.property.propertyagent.modal.agent.agent_property_rent_details

data class AgentPropertyRentDetailsResponse(
    val message: String,
    val other_charge_data: List<OtherChargeData>,
    val rent_data: RentData,
    val status: Int
)