package com.property.propertyagent.modal.owner.owner_payment_list_of_properties

data class OwnerPaymentListOfPropertiesProperty(
    val expected_amount: String ,
    val first_owner_payament: OwnerPaymentListOfPropertiesFirstOwnerPayament ,
    val frequency: Int ,
    val id: Int ,
    val last_owner_payament: OwnerPaymentListOfPropertiesLastOwnerPayament ,
    val latitude: String ,
    val longitude: String ,
    val property_name: String ,
    val property_priority_image: OwnerPaymentListOfPropertiesPropertyPriorityImage ,
    val property_reg_no: String ,
    val property_rent_frequency: OwnerPaymentListOfPropertiesPropertyRentFrequency ,
    val property_to: Int,
    val selling_price: String,
    val rent: String,
)