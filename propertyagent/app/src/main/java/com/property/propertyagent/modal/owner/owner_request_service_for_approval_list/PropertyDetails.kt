package com.property.propertyagent.modal.owner.owner_request_service_for_approval_list

data class PropertyDetails(
    val category: Int,
    val city: String,
    val city_rel: CityRel,
    val country: String,
    val country_rel: CountryRel,
    val id: Int,
    val owner_id: Int,
    val property_name: String,
    val property_priority_image: PropertyPriorityImage,
    val property_reg_no: String,
    val property_to: Int,
    val state: String,
    val state_rel: StateRel,
    val street_address_1: String,
    val street_address_2: String,
    val zip_code: Int,
    val zipcode_rel: ZipcodeRel,
    val latitude: String,
    val longitude: String,
)