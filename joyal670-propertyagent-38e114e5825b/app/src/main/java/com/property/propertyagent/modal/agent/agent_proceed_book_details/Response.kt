package com.property.propertyagent.modal.agent.agent_proceed_book_details

data class Response(
    val pending_amount: String,
    val property_id: Int,
    val token_amount: String,
    val tour_id: String,
    val user_id: Int,
    val user_property_id: Int
)