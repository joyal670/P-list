package com.iroid.jeetmeet.modal.student.submit_leave_application

data class StudentSubmitLeaveApplicationData(
    val apply_date: String,
    val attachment: String,
    val attachment_url: String,
    val from_date: String,
    val from_time: String,
    val id: Int,
    val leave_category: Int,
    val leave_days: Int,
    val reason: String,
    val status: Int,
    val student: Int,
    val to_date: String,
    val to_time: String
)