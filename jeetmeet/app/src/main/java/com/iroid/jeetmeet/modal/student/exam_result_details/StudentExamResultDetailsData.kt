package com.iroid.jeetmeet.modal.student.exam_result_details

data class StudentExamResultDetailsData(
    val answer: String,
    val id: Int,
    val mark: Int,
    val question: Int,
    val questions: StudentExamResultDetailsQuestions,
    val total_mark: Int
)