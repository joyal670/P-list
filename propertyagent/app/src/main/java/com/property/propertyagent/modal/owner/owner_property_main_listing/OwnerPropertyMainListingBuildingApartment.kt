package com.property.propertyagent.modal.owner.owner_property_main_listing

data class OwnerPropertyMainListingBuildingApartment(
    val property_priority_image : OwnerPropertyMainListingPropertyPriorityImage ,
    val occupied : Int ,
    val type_id : Int ,
    val type_details : OwnerPropertyMainListingPropertyPriorityType,
    val id : Int

)