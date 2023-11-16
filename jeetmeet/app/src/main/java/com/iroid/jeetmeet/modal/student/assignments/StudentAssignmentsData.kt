package com.iroid.jeetmeet.modal.student.assignments

data class StudentAssignmentsData(
    val all_subjects: List<StudentAssignmentsAllSubject>,
    val assignments: List<StudentAssignmentsAssignment>
)