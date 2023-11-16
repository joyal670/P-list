package com.property.propertyagent.modal.agent.agent_property_sale_details

data class AgentPropertySaleDetailsResponse(
    val emi_details: List<EmiDetail>,
    val message: String,
    val selling_price: String,
    val status: Int
)