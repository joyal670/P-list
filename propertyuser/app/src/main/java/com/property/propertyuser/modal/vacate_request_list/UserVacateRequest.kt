package com.property.propertyuser.modal.vacate_request_list

data class UserVacateRequest(
    val user_property_id: Int,
    val user_property_related: UserPropertyRelated,
    val vacate_request_id: Int,
    val vacating_date: String
)