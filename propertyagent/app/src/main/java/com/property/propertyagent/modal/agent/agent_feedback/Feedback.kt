package com.property.propertyagent.modal.agent.agent_feedback

data class Feedback(
    val comments: String,
    val email: String,
    val id: Int,
    val name: String,
    val phone: String,
    val rating: Int
)