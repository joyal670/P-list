package com.property.propertyagent.modal.agent.agent_cash_in_hand

data class CashData(
    val property_rel: PropertyRel,
    val user_rel: UserRel,
    val id: Int,
    val payment_amount: String,
    val property_id: Int,
    val user_id: Int

)