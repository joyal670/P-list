package com.property.propertyagent.modal.agent.agent_home

data class RentOverDue(
    val due_date: String,
    val id: Int,
    val prop_rel: PropRel,
    val property_id: Int,
    val rent: String,
    val user_id: Int,
    val user_rel: UserRel
)