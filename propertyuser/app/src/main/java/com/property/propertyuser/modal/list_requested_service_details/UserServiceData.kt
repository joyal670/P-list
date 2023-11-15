package com.property.propertyuser.modal.list_requested_service_details

data class UserServiceData(
    val current_page: String,
    val total_page_count: Int,
    val user_services: List<UserService>
)