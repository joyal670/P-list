package com.property.propertyuser.modal.testing

import com.property.propertyuser.modal.property_list.Event
import com.property.propertyuser.modal.property_list.Property

data class HomePropertyEventModel(
    val viewType: Int,
    val events: List<Event>,
    val property_data: Property
)