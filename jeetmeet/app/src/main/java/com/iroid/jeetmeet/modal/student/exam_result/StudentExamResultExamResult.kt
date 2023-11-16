package com.iroid.jeetmeet.modal.student.exam_result

data class StudentExamResultExamResult(
    val category: Int,
    val `class`: Int,
    val classes: StudentExamResultClasses,
    val division: Int,
    val divisions: StudentExamResultDivisions,
    val edate: String,
    val examfrom: String,
    val exams_category: StudentExamResultExamsCategory,
    val examto: String,
    val id: Int,
    val instruction_id: Int,
    val instructions: StudentExamResultInstructions,
    val name: String,
    val room: Int,
    val rooms: StudentExamResultRooms,
    val subject: Int,
    val subjects: StudentExamResultSubjects
)