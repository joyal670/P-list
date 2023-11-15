package com.property.propertyagent.modal.owner.owner_payment_list_of_properties

data class OwnerPaymentListOfPropertiesLastOwnerPayament(
    val amount: String,
    val amount_type: Int,
    val date: String,
    val id: Int,
    val payment_type: Int,
    val property_id: Int
)