package com.property.propertyuser.modal.single_vacate_request_details

data class UserVacateRequest(
    val id: Int,
    val status: Int,
    val user_property_id: Int,
    val user_property_related: UserPropertyRelated,
    val vacating_date: String
)