package com.iroid.jeetmeet.modal.student.edit_leave

data class StudentEditLeaveResponse(
    val category_list: List<StudentEditLeaveCategory>,
    val leave_applied: StudentEditLeaveApplied,
    val status: Int
)