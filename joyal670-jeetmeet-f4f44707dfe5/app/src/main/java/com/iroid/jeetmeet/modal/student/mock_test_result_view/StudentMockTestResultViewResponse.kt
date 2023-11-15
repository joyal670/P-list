package com.iroid.jeetmeet.modal.student.mock_test_result_view

data class StudentMockTestResultViewResponse(
    val gainedmark: Int,
    val results: List<StudentMockTestResultViewResult>,
    val status: Int,
    val test: StudentMockTestResultViewTest,
    val totalmark: Int
)