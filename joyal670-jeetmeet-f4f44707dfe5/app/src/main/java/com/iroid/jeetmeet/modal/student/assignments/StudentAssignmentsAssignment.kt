package com.iroid.jeetmeet.modal.student.assignments

data class StudentAssignmentsAssignment(
    val academic_year: Int,
    val academic_yearname: StudentAssignmentsAcademicYearname,
    val attachment_url: String,
    val `class`: Int,
    val classname: StudentAssignmentsClassname,
    val deadline_date: String,
    val description: String,
    val division: Int,
    val divisionname: StudentAssignmentsDivisionname,
    val `file`: String,
    val name: String,
    val subject: Int,
    val subjectname: Subjectname
)