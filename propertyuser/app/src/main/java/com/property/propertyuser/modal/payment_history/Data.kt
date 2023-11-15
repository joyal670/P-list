package com.property.propertyuser.modal.payment_history

data class Data(
    val current_page: String,
    val payment_history: List<PaymentHistory>,
    val total_page_count: Int
)