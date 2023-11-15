package com.property.propertyuser.modal.property_list

data class Property(
    val Bathroom: Int,
    val Beds: Int,
    val agent_id: String,
    val apartments: Int,
    val area: Int,
    val category: Int,
    val distance: Int,
    val documents: List<Document>,
    val floors: Int,
    val id: Int,
    var is_favourite: Int,
    val is_featured: Int,
    val mrp: String,
    val owner_id: Int,
    val property_reg_no: String,
    val property_name:String,
    val property_to: Int,
    val rating: String,
    val rent: String,
    val selling_price: String,
    var viewType: Int =1
)