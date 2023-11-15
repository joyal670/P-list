package com.ncomfortsagent.model.faq

data class AgentFaqFaqData(
    val current_page: String,
    val faqs: List<AgentFaqFaq>,
    val total_page_count: Int
)