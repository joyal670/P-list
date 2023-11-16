package com.iroid.jeetmeet.modal.student.exam_start

data class StudentExamStartResponse(
    val question: List<StudentExamStartQuestion>,
    val status: Int,
    val totalDuration: String,
    val total_qustion: Int
)