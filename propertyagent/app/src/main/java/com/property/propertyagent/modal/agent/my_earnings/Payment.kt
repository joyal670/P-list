package com.property.propertyagent.modal.agent.my_earnings

data class Payment(
    val amount: String,
    val id: Int,
    val property_id: Int,
    val property_rel: PropertyRel,
    val status: Int
)