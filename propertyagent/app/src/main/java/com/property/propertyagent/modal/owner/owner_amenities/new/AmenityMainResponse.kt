package com.property.propertyagent.modal.owner.owner_amenities.new

data class AmenityMainResponse(
    val amenitiesData : List<Amenity_subTitle> ,
    val details : List<Amenity_Detail>
)