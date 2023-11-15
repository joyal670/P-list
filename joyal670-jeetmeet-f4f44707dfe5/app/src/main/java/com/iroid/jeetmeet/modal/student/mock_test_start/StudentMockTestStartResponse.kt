package com.iroid.jeetmeet.modal.student.mock_test_start

data class StudentMockTestStartResponse(
    val questions: List<StudentMockTestStartQuestion>,
    val status: Int,
    val total_time: String
)