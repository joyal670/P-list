package com.ncomfortsagent.model.property_details

data class AgentPropertyDetailsOtherFrequencyForUser(
    val days: Int,
    val frequency_id: Int,
    val other_fee: String,
    val property_id: Int,
    val rent: String,
    val service_fee: String,
    val type: String
)