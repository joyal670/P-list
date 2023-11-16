package com.property.propertyagent.modal.owner.owner_payment_history_list

data class OwnerPaymentHistoryListPropertyPayment(
    val current_page: String ,
    val payments: List<OwnerPaymentHistoryListPayment> ,
    val total_page_count: Int
)