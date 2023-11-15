package com.property.propertyuser.modal.fetch_requested_property_data

data class ResponseData(
    val price_range: PriceRange,
    val property_category: List<PropertyCategory>
)