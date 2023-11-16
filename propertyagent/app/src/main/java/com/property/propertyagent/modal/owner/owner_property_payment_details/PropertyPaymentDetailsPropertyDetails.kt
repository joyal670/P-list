package com.property.propertyagent.modal.owner.owner_property_payment_details

data class PropertyPaymentDetailsPropertyDetails(
    val expected_amount: String ,
    val first_owner_payament: PropertyPaymentDetailsFirstOwnerPayament ,
    val frequency: Int ,
    val id: Int ,
    val last_owner_payament: PropertyPaymentDetailsLastOwnerPayament ,
    val latitude: String ,
    val longitude: String ,
    val property_name: String ,
    val property_priority_image: PropertyPaymentDetailsPropertyPriorityImage ,
    val property_reg_no: String ,
    val property_rent_frequency: PropertyPaymentDetailsPropertyRentFrequency ,
    val property_to: Int,
    val selling_price: String,
    val rent: String,
)