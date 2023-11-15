package com.iroid.jeetmeet.modal.parent.my_account_payment

data class ParentMyAccountPaymentResponse(
    val fee_details: List<ParentMyAccountPaymentFeeDetail>,
    val payment: ParentMyAccountPaymentPayment,
    val settings: ParentMyAccountPaymentSettings,
    val status: Int,
    val student: ParentMyAccountPaymentStudent,
    val data: String
)