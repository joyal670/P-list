package com.iroid.jeetmeet.modal.student.edit_leave

data class StudentEditLeaveApplied(
    val attachment: String,
    val attachment_url: String,
    val from_date: String,
    val from_time: String,
    val id: Int,
    val leave_category: Int,
    val leave_categoryname: StudentEditLeaveCategoryname,
    val reason: String,
    val to_date: String,
    val to_time: String
)