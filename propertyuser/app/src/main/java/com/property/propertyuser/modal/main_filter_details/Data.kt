package com.property.propertyuser.modal.main_filter_details

data class Data(
    val amenities: List<Amenity>,
    val category: List<Category>,
    val details: List<Detail>,
    val frequencies: List<Frequency>
)