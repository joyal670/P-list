package com.iroid.jeetmeet.modal.parent.student_view

data class ParentStudentViewData(
    val `class`: String,
    val classname: ParentStudentViewClassname,
    val code: String,
    val division: Int,
    val divisions: ParentStudentViewDivisions,
    val dob: String,
    val first_name: String,
    val gender: String,
    val last_name: String,
    val middle_name: String,
    val phone: String,
    val profile_image: String,
    val profile_image_url: String
)