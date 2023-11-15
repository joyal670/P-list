package com.ncomfortsagent.model.login

data class AgentLoginResponse(
    val `data`: AgentLoginData,
    val response: String,
    val status: Int
)