package com.ncomfortsagent.model.enquiry

data class AgentHomeEnquiryUserProperty(
    val feature_details: String,
    val id: Int,
    val property_details: AgentHomeEnquiryPropertyDetails,
    val property_id: Int,
    val property_priority_image: AgentHomeEnquiryPropertyPriorityImage,
    val user_id: Int,
    val user_rel: AgentHomeEnquiryUserRel,
    val enquiry_status: Int,
    val view_status: Int
)