package com.iroid.jeetmeet.modal.student.exam_schedule_preview

data class StudentExamSchedulePreviewExam(
    val category: Int,
    val `class`: Int,
    val classes: StudentExamSchedulePreviewClasses,
    val division: Int,
    val divisions: StudentExamSchedulePreviewDivisions,
    val edate: String,
    val examfrom: String,
    val exams_category: StudentExamSchedulePreviewExamsCategory,
    val examto: String,
    val id: Int,
    val instruction_id: Int,
    val instructions: StudentExamSchedulePreviewInstructions,
    val name: String,
    val subject: Int,
    val subjects: Subjects
)