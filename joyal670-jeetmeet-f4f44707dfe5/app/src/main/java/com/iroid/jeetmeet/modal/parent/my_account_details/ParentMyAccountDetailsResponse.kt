package com.iroid.jeetmeet.modal.parent.my_account_details

data class ParentMyAccountDetailsResponse(
    val advance: String,
    val advance_balance: Int,
    val button_key: Int,
    val deposit: Int,
    val fee: List<ParentMyAccountDetailsFee>,
    val status: Int,
    val sub_total: Int,
    val total: Int,
    val student_code: String,
    val student_id: Int,
    val posting_ids: List<Int>,
    val data: String
)