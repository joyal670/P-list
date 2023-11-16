package com.ncomfortsagent.model.enquiry

data class AgentHomeEnquiryTourData(
    val current_page: String,
    val total_page_count: Int,
    val user_properties: List<AgentHomeEnquiryUserProperty>
)