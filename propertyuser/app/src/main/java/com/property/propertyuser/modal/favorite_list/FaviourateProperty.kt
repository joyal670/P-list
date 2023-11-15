package com.property.propertyuser.modal.favorite_list

data class FaviourateProperty(
    val Bathroom: Int,
    val Beds: Int,
    val apartments: Int,
    val area: Int,
    val category: Int,
    val conference_hall: Int,
    val documents: List<Document>,
    val floors: Int,
    val id: Int,
    var is_favourite: Int,
    val is_featured: Int,
    val mrp: String,
    val owner_id: Int,
    val property_name: String,
    val property_reg_no: String,
    val property_to: Int,
    val property_type: String,
    val rating: String,
    val rent: String,
    val selling_price: String
)