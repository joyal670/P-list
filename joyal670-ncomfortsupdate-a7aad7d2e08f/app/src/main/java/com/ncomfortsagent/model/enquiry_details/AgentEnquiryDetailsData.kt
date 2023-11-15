package com.ncomfortsagent.model.enquiry_details

data class AgentEnquiryDetailsData(
    val date: String,
    val enquiry_status: String,
    val feature_details: String,
    val id: Int,
    val property_details: AgentEnquiryDetailsPropertyDetails,
    val property_id: Int,
    val property_priority_image: AgentEnquiryDetailsPropertyPriorityImage,
    val user_id: Int,
    val user_rel: AgentEnquiryDetailsUserRel
)