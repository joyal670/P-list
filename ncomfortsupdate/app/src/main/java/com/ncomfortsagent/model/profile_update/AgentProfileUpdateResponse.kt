package com.ncomfortsagent.model.profile_update

data class AgentProfileUpdateResponse(
    val `data`: AgentProfileUpdateData,
    val response: String,
    val status: Int
)