package com.iroid.jeetmeet.modal.student.exam_result

data class StudentExamResultResponse(
    val exam_result: List<StudentExamResultExamResult>,
    val status: Int
)