package com.property.propertyagent.modal.owner.owner_request_service_for_approval_list

data class RequestDetail(
    val date: String ,
    val description: String ,
    val id: Int ,
    val property_details: PropertyDetails ,
    val service_id: Int ,
    val service_related: ServiceRelated ,
    var status: Int ,
    val time: String ,
    val user_id: Int ,
    val user_property_id: Int ,
    val user_property_related: UserPropertyRelated ,
    val user_service_related: UserServiceRelated
)