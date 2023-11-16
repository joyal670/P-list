package com.property.propertyagent.modal.agent.agent_my_request_list

data class UserPropertyRelated(
    val id: Int,
    val latitude: String,
    val longitude: String,
    val property_name: String,
    val property_reg_no: String,
    val property_to: Int
)