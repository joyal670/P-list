package com.ncomfortsagent.model.property_details

data class AgentPropertyDetailsPropertyDetails(
    val Apartments: Int,
    val Area: Int,
    val Bathroom: Int,
    val Beds: Int,
    val Conference_Hall: Int,
    val Floors: Int,
    val Restaurant: Int,
    val amenity_categories: List<AgentPropertyDetailsAmenityCategory>,
    val bhk: Int,
    val category: Int,
    val city: String,
    val description: String,
    val documents: List<AgentPropertyDetailsDocument>,
    val furnished: Int,
    val id: Int,
    val location: String,
    val mrp: String,
    val occupied: Int,
    val other_frequency_for_user: List<AgentPropertyDetailsOtherFrequencyForUser>,
    val property_name: String,
    val property_reg_no: String,
    val property_to: Int,
    val rent: String,
    val security_deposit: String,
    val selling_price: String,
    val token_amount: String,
    val type_details: AgentPropertyDetailsTypeDetails,
    val type_id: Int,
    val utilization: Int,
    val utility_amount: String,
    val maintenance_amount: String,
    val utility_status: Boolean,
    val maintenance_status: Boolean,
    val frequency: String,
    val property_details: List<AgentPropertyDetailsList>
)