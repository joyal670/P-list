package com.property.propertyagent.modal.owner.owner_payment_history_details

data class OwnerPaymentHistoryDetailsServiceDetails(
    val description: String,
    val id: Int,
    val service_id: Int,
    val service_related: OwnerPaymentHistoryDetailsServiceRelated
)