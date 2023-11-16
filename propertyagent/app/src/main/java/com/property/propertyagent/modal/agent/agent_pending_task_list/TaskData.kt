package com.property.propertyagent.modal.agent.agent_pending_task_list

data class TaskData(
    val current_page: String,
    val task: List<Task>,
    val total_page_count: Int
)