package com.property.propertyagent.modal.owner.owner_property_main_listing

data class OwnerPropertyMainListing1Response(
    val current_page: String ,
    val owner: List<OwnerPropertyMainListingOwner> ,
    val total_page_count: Int
)