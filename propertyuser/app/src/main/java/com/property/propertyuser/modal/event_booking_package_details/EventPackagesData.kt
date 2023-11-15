package com.property.propertyuser.modal.event_booking_package_details

data class EventPackagesData(
    val event_package_image: EventPackageImage,
    val id: Int,
    val package_name: String,
    val price: String
)