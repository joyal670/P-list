package com.property.propertyuser.modal.show_all_near_by_property_details

data class Data(
    val main_property: MainProperty,
    val near_properties: List<NearProperty>
)