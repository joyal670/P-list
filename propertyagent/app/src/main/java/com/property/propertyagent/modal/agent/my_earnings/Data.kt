package com.property.propertyagent.modal.agent.my_earnings

data class Data(
    val current_page: String,
    val payments: List<Payment>,
    val total_page_count: Int
)