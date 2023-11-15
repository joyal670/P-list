package com.iroid.jeetmeet.modal.student.leave_apply

data class StudentLeaveApplyData(
    val apply_date: String,
    val attachment: String,
    val attachment_url: String,
    val from_date: String,
    val id: Int,
    val leave_category: Int,
    val leave_categoryname: StudentLeaveApplyLeaveCategoryname,
    val leave_days: Int,
    val reason: String,
    val status: Int,
    val student: Int,
    val studentname: StudentLeaveApplyStudentname,
    val to_date: String
)