package com.property.propertyagent.modal.agent.agent_my_request_list

data class Requested(
    val date: String,
    val id: Int,
    val owner_id: Int,
    val owner_rel: OwnerRel,
    val property_details: PropertyDetails,
    val property_id: Int,
    val property_priority_image: PropertyPriorityImage,
    val time: String,
    val type: String,
    val user_id: Int,
    val user_property_related: UserPropertyRelated,
    val user_rel: UserRel
)