package com.iroid.jeetmeet.modal.student.online_class

data class StudentOnlineClassData(
    val `class`: Int,
    val classname: StudentOnlineClassClassname,
    val date: String,
    val division: Int,
    val divisions: StudentOnlineClassDivisions,
    val end_time: String,
    val id: Int,
    val start_time: String,
    val subject: String,
    val subjects: StudentOnlineClassSubjects,
    val title: String
)