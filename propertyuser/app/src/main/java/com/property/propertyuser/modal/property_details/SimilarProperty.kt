package com.property.propertyuser.modal.property_details

data class SimilarProperty(
    val Bathroom: Int,
    val Beds: Int,
    val agent_id: String,
    val apartments: Int,
    val area: Int,
    val category: Int,
    val city_rel: String,
    val country_rel: String,
    val documents: List<Document>,
    val floors: Int,
    val id: Int,
    var is_favourite: Int,
    val is_featured: Int,
    val latitude: String,
    val longitude: String,
    val mrp: String,
    val owner_id: Int,
    val property_reg_no: String,
    val property_to: Int,
    val rating: String,
    val rent: String,
    val selling_price: String,
    val state_rel: String
)