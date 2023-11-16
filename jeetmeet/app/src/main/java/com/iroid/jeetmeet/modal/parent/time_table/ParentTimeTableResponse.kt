package com.iroid.jeetmeet.modal.parent.time_table

data class ParentTimeTableResponse(
    val `data`: LinkedHashMap<String, ArrayList<ParentTimeTable>>?,
    val days: LinkedHashMap<String, String>?,
    val status: Int
)