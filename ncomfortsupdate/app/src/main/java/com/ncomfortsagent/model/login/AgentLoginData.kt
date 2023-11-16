package com.ncomfortsagent.model.login

data class AgentLoginData(
    val api_token: String,
    val email: String,
    val id: Int,
    val name: String,
    val password: String,
    val phone: String,
    val profile_pic: String,
    val status: Int,
    val updated_at: String,
    val agent_id: Int
)