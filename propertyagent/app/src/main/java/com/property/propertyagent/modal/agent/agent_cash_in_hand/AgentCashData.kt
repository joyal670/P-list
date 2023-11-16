package com.property.propertyagent.modal.agent.agent_cash_in_hand

data class AgentCashData(
    val cash_data: List<CashData>,
    val current_page: String,
    val total_page_count: Int
)