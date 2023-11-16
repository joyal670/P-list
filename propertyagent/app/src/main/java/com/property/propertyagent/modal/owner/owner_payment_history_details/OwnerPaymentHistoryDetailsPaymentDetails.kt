package com.property.propertyagent.modal.owner.owner_payment_history_details

data class OwnerPaymentHistoryDetailsPaymentDetails(
    val amount: String ,
    val amount_type: Int ,
    val date: String ,
    val id: Int ,
    val payment_received: OwnerPaymentHistoryDetailsPaymentReceived ,
    val payment_type: Int ,
    val property_id: Int ,
    val reference_id: Int
)