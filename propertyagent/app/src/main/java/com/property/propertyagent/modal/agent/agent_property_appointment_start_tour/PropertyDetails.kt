package com.property.propertyagent.modal.agent.agent_property_appointment_start_tour

data class PropertyDetails(
    val id: Int,
    val mrp: String,
    val property_name: String,
    val property_priority_image: PropertyPriorityImage,
    val property_reg_no: String,
    val property_to: Int,
    val rent: String,
    val security_deposit: String,
    val selling_price: String,
    val token_amount: String
)