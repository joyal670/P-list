package com.iroid.jeetmeet.modal.student.subjects

data class StudentSubjectsReponse(
    val `class`: StudentSubjectClass,
    val division: StudentSubjectDivision,
    val status: Int,
    val subjects: List<StudentSubjectSubject>
)