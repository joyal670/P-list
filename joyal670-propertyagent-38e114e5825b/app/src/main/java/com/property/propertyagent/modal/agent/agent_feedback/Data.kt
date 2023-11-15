package com.property.propertyagent.modal.agent.agent_feedback

data class Data(
    val current_page: String,
    val feedbacks: List<Feedback>,
    val total_page_count: Int
)