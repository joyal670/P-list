package com.iroid.jeetmeet.modal.student.exam_schedule_preview

data class StudentExamSchedulePreviewResponse(
    val exam: StudentExamSchedulePreviewExam,
    val status: Int,
    val student: StudentExamSchedulePreviewStudent,
    val totalDuration: String,
    val total_qustion: Int,
    val totalmark: Int
)