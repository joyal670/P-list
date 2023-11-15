package com.property.propertyagent.modal.owner.owner_payment_history_list

data class OwnerPaymentHistoryListPayment(
    val amount: String,
    val amount_type: Int,
    val date: String,
    val id: Int,
    val payment_type: Int
)