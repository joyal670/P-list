package com.ncomfortsagent.model.property

data class PropertyNew1Response(
    val current_page: String,
    val total_page_count: Int,
    val user_properties: List<PropertyNewUserProperty>
)