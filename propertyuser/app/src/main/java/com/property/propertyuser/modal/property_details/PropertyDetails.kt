package com.property.propertyuser.modal.property_details

data class PropertyDetails(
    val Bathroom: Int,
    val Beds: Int,
    val assigned_agent_details: AgentDetails,
    val agent_id: String,
    val amenity_categories: List<AmenityCategory>,
    val apartments: Int,
    val area: Int,
    val category: Int,
    val city: String,
    val description: String,
    val documents: List<Document>,
    val floor_plans: List<FloorPlan>,
    val floors: Int,
    val furnished: Any,
    val id: Int,
    val is_favourite: Int,
    val is_featured: Int,
    val latitude: String,
    val location: String,
    val longitude: String,
    val mrp: String,
    val owner_id: Int,
    val property_reg_no: String,
    val property_to: Int,
    val rating: String,
    val rent: String,
    val selling_price: String,
    val total_rating_count: Int,
    val property_name : String,
    val property_type : String,
    val occupied : Any,
    val frequency: String
)