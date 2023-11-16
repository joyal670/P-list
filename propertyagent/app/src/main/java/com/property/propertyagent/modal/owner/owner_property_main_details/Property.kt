package com.property.propertyagent.modal.owner.owner_property_main_details

data class Property(
    val contract_file: String,
    val documents: List<Document>,
    val frequency: Int,
    val id: Int,
    val is_featured: Int,
    val income: String ,
    val net_worth: String ,
    val outstanding_due: String ,
    val pending: String ,
    val owner_amount: String,
    val property_agent: PropertyAgent,
    val property_name: String,
    val property_reg_no: String,
    val property_to: Int,


    val rent: String,
    val selling_price: String,
    val commission_in: String,
    val commission: Int,
    val management_charge: Int,
    val property_rent_frequency: PropertyRentFrequency
)