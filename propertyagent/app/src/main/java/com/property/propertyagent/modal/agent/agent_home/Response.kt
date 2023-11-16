package com.property.propertyagent.modal.agent.agent_home

data class Response(
    val appoinments: List<Appoinment>,
    val appointment_count: Int,
    val rent_over_due: List<RentOverDue>,
    val total_assigned_count: Int,
    val zone: String
)