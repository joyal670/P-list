package com.property.propertyagent.modal.owner.owner_building_details

data class OwnerBuildingDetailsListBuildingApartment(
    val frequency: Int ,
    val id: Int ,
    val occupied: Int ,
    val occupied_from: String ,
    val occupied_to: String ,
    val property_name: String ,
    val property_priority_image: OwnerBuildingDetailsListPropertyPriorityImage ,
    val property_reg_no: String ,
    val property_to: Int ,
    val rent_out_count: Int ,
    val type_details: OwnerBuildingDetailsListTypeDetails ,
    val type_id: Int ,
    val vacated_since: String
)