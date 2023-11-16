package com.property.propertyagent.modal.agent.agent_calender_task_list

data class Task(
    var completed: Int ,
    val id: Int ,
    val task_date: String ,
    val task_time: String ,
    val title: String
)