package com.property.propertyagent.modal.agent.agent_calender_task_count

data class AgentCalenderTaskCountResponse(
    val agent_task: List<AgentTask>,
    val status: Int
)