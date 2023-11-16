package com.property.propertyagent.modal.agent.agent_start_owner_tour_property_details

data class PropertyDetails(
    val Bathroom: Int,
    val Beds: Int,
    val apartments: Int,
    val area: Int,
    val conference_hall: Int,
    val floors: Int,
    val id: Int,
    val is_builder : String,
    val furnished: String,
    val latitude: String,
    val longitude: String,
    val mrp: String,
    val contract_start_date: String,
    val contract_end_date: String,
    val owner_id: Int,
    val property_details: List<PropertyDetail>,
    val property_name: String,
    val property_priority_image: PropertyPriorityImage,
    val property_reg_no: String,
    val property_to: Int,
    val rent: String,
    val security_deposit: String,
    val selling_price: String,
    val token_amount: String
)