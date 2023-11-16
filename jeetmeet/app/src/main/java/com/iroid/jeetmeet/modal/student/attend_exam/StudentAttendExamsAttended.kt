package com.iroid.jeetmeet.modal.student.attend_exam

data class StudentAttendExamsAttended(
    val category: Int,
    val `class`: Int,
    val classes: StudentAttendExamClasses,
    val division: Int,
    val divisions: StudentAttendExamDivisions,
    val edate: String,
    val examfrom: String,
    val exams_category: StudentAttendExamsCategory,
    val examto: String,
    val id: Int,
    val instruction_id: Int,
    val instructions: StudentAttendExamInstructions,
    val name: String,
    val room: Int,
    val rooms: StudentAttendExamRooms,
    val subject: Int,
    val subjects: StudentAttendExamSubjects
)