package com.property.propertyagent.modal.agent.agent_owner_completed_list

data class UserProperty(
    val id: Int,
    val owner_rel: OwnerRel,
    val property_id: Int,
    val property_priority_image: PropertyPriorityImage,
    val user_property_related: UserPropertyRelated
)