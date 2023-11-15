package com.property.propertyuser.modal.payment_history

data class PaymentHistory(
    val amount: String,
    val amount_type: Int,
    val date: String,
    val id: Int,
    val payment_type: Int,
    val property_id: Int,
    val reference_id: Int,
    val service: String,
    val service_description: String
)