package com.iroid.jeetmeet.modal.parent.my_account_partial_pay

data class ParentMyAccountPartialPayResponse(
    val fee_details: List<ParentMyAccountPartialPayFeeDetail>,
    val payment: ParentMyAccountPartialPayPayment,
    val settings: ParentMyAccountPartialPaySettings,
    val status: Int,
    val student: ParentMyAccountPartialPayStudent,
    val data: String
)