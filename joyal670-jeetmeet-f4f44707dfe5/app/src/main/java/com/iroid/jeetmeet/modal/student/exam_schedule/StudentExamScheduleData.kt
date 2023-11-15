package com.iroid.jeetmeet.modal.student.exam_schedule

data class StudentExamScheduleData(
    val attant: List<StudentExamScheduleAttant>,
    val category: Int,
    val `class`: Int,
    val classes: StudentExamScheduleClasses,
    val division: Int,
    val divisions: StudentExamScheduleDivisions,
    val edate: String,
    val examfrom: String,
    val exams_category: StudentExamScheduleExamsCategory,
    val examto: String,
    val id: Int,
    val instruction_id: Int,
    val instructions: StudentExamScheduleInstructions,
    val name: String,
    val room: Int,
    val rooms: StudentExamScheduleRooms,
    val subject: Int,
    val subjects: Subjects
)