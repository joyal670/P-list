package com.property.propertyuser.modal.property_details

data class PropertyData(
    val property_details: PropertyDetails,
    val similar_properties: List<SimilarProperty>
)