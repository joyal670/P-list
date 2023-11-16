package com.property.propertyagent.modal.agent.agent_assigned_property_list

data class UserProperty(
    val city: String,
    val city_rel: CityRel,
    val country: String,
    val country_rel: CountryRel,
    val id: Int,
    val latitude: String,
    val is_builder: String,
    val longitude: String,
    val property_name: String,
    val property_priority_image: PropertyPriorityImage,
    val property_to: Int,
    val state: String,
    val state_rel: StateRel
)