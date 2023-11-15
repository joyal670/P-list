package com.property.propertyuser.modal.desired_property_request_list

data class Data(
    val current_page: String,
    val requested_list: List<Requested>,
    val total_page_count: Int
)