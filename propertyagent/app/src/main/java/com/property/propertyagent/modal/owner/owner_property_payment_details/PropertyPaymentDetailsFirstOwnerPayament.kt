package com.property.propertyagent.modal.owner.owner_property_payment_details

data class PropertyPaymentDetailsFirstOwnerPayament(
    val amount: String,
    val amount_type: Int,
    val date: String,
    val id: Int,
    val payment_type: Int,
    val property_id: Int
)