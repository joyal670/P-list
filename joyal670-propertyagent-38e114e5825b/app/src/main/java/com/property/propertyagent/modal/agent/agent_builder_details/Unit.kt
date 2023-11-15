package com.property.propertyagent.modal.agent.agent_builder_details

data class Unit(
    val expected_amount: String,
    val furnished: String,
    val id: Int,
    val occupied: String,
    val property_priority_image: PropertyPriorityImage,
    val property_reg_no: String,
    val unit_name: String
)