package com.property.propertyagent.modal.owner.owner_request_service_for_approval_list

data class Data(
    val current_page: String,
    val request_details: List<RequestDetail>,
    val total_page_count: Int
)