package com.property.propertyagent.modal.agent.agent_package_details

data class Package(
    val actual_amount: String,
    val description: String,
    val discount_amount: String,
    val end_date: String,
    val frequency_id: Int,
    val id: Int,
    val offer_package_name: String,
    val package_features: List<PackageFeature>,
    val property_id: Int,
    val start_date: String
)