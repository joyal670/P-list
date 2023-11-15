package com.ncomfortsagent.model.profile_update

data class AgentProfileUpdateData(
    val address: String,
    val email: String,
    val id: Int,
    val name: String,
    val phone: String,
    val profile_pic: String
)