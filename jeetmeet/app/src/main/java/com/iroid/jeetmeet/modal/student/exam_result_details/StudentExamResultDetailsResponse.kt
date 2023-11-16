package com.iroid.jeetmeet.modal.student.exam_result_details

data class StudentExamResultDetailsResponse(
    val attained_mark: Int,
    val `data`: List<StudentExamResultDetailsData>,
    val exam_details: StudentExamResultDetailsExamDetails,
    val grade: Any,
    val status: Int,
    val student: StudentExamResultDetailsStudent,
    val total_mark: Int,
    val total_questions: Int
)