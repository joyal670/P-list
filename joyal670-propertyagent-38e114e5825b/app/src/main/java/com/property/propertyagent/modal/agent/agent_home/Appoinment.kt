package com.property.propertyagent.modal.agent.agent_home

data class Appoinment(
    val date: String,
    val id: Int,
    val owner_details: OwnerDetails,
    val owner_id: Int,
    val property_details: PropertyDetails,
    val property_id: Int,
    val time: String,
    val tour_id: Int,
    val type: Int,
    val user_details: UserDetails,
    val user_id: Int
)