package com.iroid.jeetmeet.modal.student.attend_exam

data class StudentAttendExamResponse(
    val exams_attended: List<StudentAttendExamsAttended>,
    val status: Int
)