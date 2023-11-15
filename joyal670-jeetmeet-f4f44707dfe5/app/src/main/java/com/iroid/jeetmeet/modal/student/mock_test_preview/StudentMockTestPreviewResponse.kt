package com.iroid.jeetmeet.modal.student.mock_test_preview

data class StudentMockTestPreviewResponse(
    val status: Int,
    val student: StudentMockTestPreviewStudent,
    val test: StudentMockTestPreviewTest,
    val totalmark: String
)