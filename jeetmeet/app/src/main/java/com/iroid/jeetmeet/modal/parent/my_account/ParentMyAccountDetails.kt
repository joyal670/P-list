package com.iroid.jeetmeet.modal.parent.my_account

data class ParentMyAccountDetails(
    val advance: String,
    val `class`: String,
    val current_due: String,
    val currrent_outstanding: Int,
    val division: String,
    val id: Int,
    val payment_status: String,
    val previous_due: String,
    val student_code: String,
    val student_name: String,
    val total_due: String
)