package com.property.propertyagent.modal.agent.agent_cash_in_hand

data class AgentCashInHandTotalResponse(
	val canPay: Int,
	val total_amount: String,
	val status: Int
)
