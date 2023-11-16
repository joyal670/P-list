package com.iroid.jeetmeet.modal.parent.dashboard

data class ParentDashboardData(
    val count_class: Int,
    val count_parents: Int,
    val count_student: Int,
    val count_teacher: Int,
    val notice: List<ParentDashboardNotice>,
    val parent: ParentDashboardParent
)