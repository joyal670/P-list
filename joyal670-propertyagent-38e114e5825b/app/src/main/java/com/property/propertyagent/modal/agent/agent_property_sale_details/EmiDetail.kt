package com.property.propertyagent.modal.agent.agent_property_sale_details

data class EmiDetail(
    val descriptions: String,
    val emi_amount: Int,
    val emi_count: Int,
    val id: Int,
    val interest_rate: Int,
    val period_type: String
)