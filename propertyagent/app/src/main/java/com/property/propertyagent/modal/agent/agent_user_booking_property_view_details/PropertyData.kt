package com.property.propertyagent.modal.agent.agent_user_booking_property_view_details

data class PropertyData(
    val Bathroom: Int,
    val Beds: Int,
    val Restaurant: Int,
    val apartments: Int,
    val area: Int,
    val city: String,
    val city_rel: CityRel,
    val conference_hall: Int,
    val contract_end_date: String,
    val contract_start_date: String,
    val floors: Int,
    val furnished: Any,
    val id: Int,
    val mrp: String,
    val property_details: List<PropertyDetail>,
    val property_name: String,
    val property_reg_no: String,
    val property_to: Int,
    val rating: String,
    val rating_count: Any,
    val rent: String,
    val selling_price: String
)