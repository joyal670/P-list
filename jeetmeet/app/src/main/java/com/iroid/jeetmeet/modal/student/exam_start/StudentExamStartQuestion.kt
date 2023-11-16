package com.iroid.jeetmeet.modal.student.exam_start

data class StudentExamStartQuestion(
    val id: Int,
    val option: List<StudentExamStartOption>,
    val question: String,
    val type: Int,
    val types: StudentExamStartTypes
)