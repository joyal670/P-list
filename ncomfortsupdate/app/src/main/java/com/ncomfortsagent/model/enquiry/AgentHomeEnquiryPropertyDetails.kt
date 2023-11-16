package com.ncomfortsagent.model.enquiry

data class AgentHomeEnquiryPropertyDetails(
    val id: Int,
    val mrp: String,
    val property_name: String,
    val property_reg_no: String,
    val property_to: Int,
    val rent: String,
    val selling_price: String
)