package com.property.propertyuser.modal.single_vacate_request_details

data class UserPropertyRelated(
    val property_id: Int,
    val property_name: String,
    val property_reg_no: String,
    val contract_start_date: String,
    val contract_end_date: String,
    val security_deposit: String,
    val due_amount: String,
    val status: Int
)