package com.property.propertyagent.modal.agent.agent_profile

data class Response(
    val account_number: String,
    val bank_name: String,
    val email: String,
    val id: Int,
    val ifsc: String,
    val image: String,
    val name: String,
    val phone: String
)