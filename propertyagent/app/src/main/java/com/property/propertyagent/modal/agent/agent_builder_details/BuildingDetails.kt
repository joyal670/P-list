package com.property.propertyagent.modal.agent.agent_builder_details

import com.property.propertyagent.modal.agent.agent_assigned_property_details.Document

data class BuildingDetails(
    val category: Int,
    val city: String,
    val city_rel: CityRel,
    val country: String,
    val country_rel: CountryRel,
    val description: String,
    val documents: List<Document>,
    val furnished: String,
    val id: Int,
    val is_featured: Int,
    val latitude: String,
    val longitude: String,
    val occupied: Any,
    var appartments_count: String,
    var occupied_count: String,
    var vacated_count: String,
    val owner_id: Int,
    val owner_rel: OwnerRel,
    val property_name: String,
    val property_reg_no: String,
    val property_to: Int,
    val property_type: String,
    val rating: String,
    val rent: String,
    val state: String,
    val state_rel: StateRel,
    val street_address_1: String,
    val street_address_2: String,
    val type_id: Int,
    val zip_code: Int,
    val zipcode_rel: ZipcodeRel
)