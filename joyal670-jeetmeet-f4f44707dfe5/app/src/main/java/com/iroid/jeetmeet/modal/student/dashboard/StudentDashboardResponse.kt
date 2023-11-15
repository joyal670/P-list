package com.iroid.jeetmeet.modal.student.dashboard

data class StudentDashboardResponse(
    val notice: List<StudentDashboardNotice>,
    val status: Int,
    val student: StudentDashboardStudent
)