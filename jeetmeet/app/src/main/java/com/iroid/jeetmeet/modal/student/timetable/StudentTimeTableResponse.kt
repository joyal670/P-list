package com.iroid.jeetmeet.modal.student.timetable

data class StudentTimeTableResponse(
    val `data`: LinkedHashMap<String, ArrayList<StudentTimeTableX1>>?,
    val days: LinkedHashMap<String, String>?,
    val status: Int
)