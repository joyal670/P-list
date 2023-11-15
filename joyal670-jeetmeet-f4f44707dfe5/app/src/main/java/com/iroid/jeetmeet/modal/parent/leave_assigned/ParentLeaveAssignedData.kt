package com.iroid.jeetmeet.modal.parent.leave_assigned

data class ParentLeaveAssignedData(
    val leave_category: Int,
    val leaveassign: Int,
    val leavecategoryname: ParentLeaveAssignedLeavecategoryname,
    val student: Int,
    val student_details: ParentLeaveAssignedStudentDetails
)