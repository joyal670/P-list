package com.iroid.jeetmeet.modal.student.dashboard

data class StudentDashboardStudent(
    val `class`: String,
    val classname: StudentDashboardClassname,
    val code: String,
    val division: Int,
    val divisions: StudentDashboardDivisions,
    val dob: String,
    val first_name: String,
    val last_name: String,
    val middle_name: String,
    val parent: Int,
    val parents: StudentDashboardParents,
    val profile_image: String,
    val profile_image_url: String,
    val reg_number: String,
    val roll_number: Int
)