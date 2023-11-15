package com.property.propertyagent.modal.owner.owner_property_main_details.new

data class OwnerPropertyMainDetailsNewProperty(
    val commission: Any,
    val commission_in: Any,
    val contract_file: Any,
    val documents: List<OwnerPropertyMainDetailsNewDocument>,
    val frequency: Any,
    val id: Int,
    val income: Any,
    val is_featured: Any,
    val management_charge: Any,
    val net_worth: Any,
    val outstanding_due: Any,
    val owner_amount: Any,
    val pending: Any,
    val property_agent: OwnerPropertyMainDetailsNewPropertyAgent,
    val property_name: Any,
    val property_reg_no: Any,
    val property_rent_frequency: OwnerPropertyMainDetailsNewPropertyRentFrequency,
    val property_to: Int,
    val rent: Any,
    val selling_price: Any,
    val status: Int
)