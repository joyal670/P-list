package com.iroid.jeetmeet.modal.parent.meetings

data class ParentMeetingsData(
    val `class`: Int,
    val classname: ParentMeetingsClassname,
    val date: String,
    val division: Int,
    val divisions: ParentMeetingsDivisions,
    val end_time: String,
    val id: Int,
    val start_time: String,
    val subject: String,
    val subjects: ParentMeetingsSubjects,
    val title: String
)