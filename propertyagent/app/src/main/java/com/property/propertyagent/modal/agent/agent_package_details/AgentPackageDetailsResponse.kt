package com.property.propertyagent.modal.agent.agent_package_details

data class AgentPackageDetailsResponse(
    val packages: List<Package>,
    val status: Int
)