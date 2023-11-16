package com.iroid.jeetmeet.modal.student.attendance

data class StudentAttendanceResponse(
    val attandance: List<StudentAttendanceAttandance>,
    val status: Int
)