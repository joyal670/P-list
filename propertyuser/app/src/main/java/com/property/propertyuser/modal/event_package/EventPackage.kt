package com.property.propertyuser.modal.event_package

data class EventPackage(
    val event_id: Int,
    val event_package_features: List<EventPackageFeature>,
    val event_package_image: EventPackageImage,
    val id: Int,
    val package_name: String,
    val price: String
)