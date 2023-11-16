package com.iroid.jeetmeet.modal.student.assigned_leave

data class StudentAssignedLeaveData(
    val id: Int,
    val leave_category: Int,
    val leavecategoryname: StudentAssignedLeavecategoryname,
    val no_of_day: Int
)