package com.property.propertyuser.modal.vacate_request_list

data class UserVacateData(
    val current_page: String,
    val total_page_count: Int,
    val user_vacate_requests: List<UserVacateRequest>
)