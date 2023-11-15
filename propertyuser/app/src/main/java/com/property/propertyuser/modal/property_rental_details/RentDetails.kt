package com.property.propertyuser.modal.property_rental_details

data class RentDetails(
    val frequency: String,
    val property_rent_frequency: PropertyRentFrequency,
    val rent: String,
    val security_deposit: String
)