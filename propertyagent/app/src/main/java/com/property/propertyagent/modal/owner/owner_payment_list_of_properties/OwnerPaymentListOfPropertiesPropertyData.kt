package com.property.propertyagent.modal.owner.owner_payment_list_of_properties

data class OwnerPaymentListOfPropertiesPropertyData(
    val current_page: String ,
    val properties: List<OwnerPaymentListOfPropertiesProperty> ,
    val total_page_count: Int
)