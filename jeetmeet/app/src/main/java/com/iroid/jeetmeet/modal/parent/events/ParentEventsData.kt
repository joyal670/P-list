package com.iroid.jeetmeet.modal.parent.events

data class ParentEventsData(
    val `class`: Int,
    val classname: ParentEventsClassname,
    val date: String,
    val division: ParentEventsDivision,
    val ex_date: String,
    val id: Int,
    val time: String,
    val title: String,
    val type: Int
)