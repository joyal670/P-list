package com.property.propertyagent.modal.agent.agent_pending_task_list


data class Task(
    val id: Int,
    val title:String,
    val task_time:String,
    val completed: Int,
    val task_date: String
)

