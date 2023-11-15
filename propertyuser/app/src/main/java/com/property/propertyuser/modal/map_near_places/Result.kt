package com.property.propertyuser.modal.map_near_places

data class Result(
    val geometry: Geometry,
    val icon: String,
    val name: String,
    val opening_hours: OpeningHours,
    val photos: List<Photo>,
    val place_id: String,
    val reference: String,
    val types: List<String>,
    val vicinity: String
)