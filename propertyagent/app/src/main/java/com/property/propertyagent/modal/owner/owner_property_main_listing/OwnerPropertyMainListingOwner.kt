package com.property.propertyagent.modal.owner.owner_property_main_listing

data class OwnerPropertyMainListingOwner(
    val appartments_count: Int ,
    val bulding_appartments: List<OwnerPropertyMainListingBuildingApartment> ,
    val category: Int ,
    val frequency: String ,
    val id: Int ,
    val income: String ,
    val is_builder: Int ,
    val net_worth: String ,
    val occupied: Int ,
    val outstanding_due: String ,
    val owner_id: Int ,
    val pending: String ,
    val property_name: String ,
    val property_priority_image: OwnerPropertyMainListingPropertyPriorityImage ,
    val property_reg_no: String ,
    val property_to: Int ,
    val property_type: String ,
    val vacated: Int
)