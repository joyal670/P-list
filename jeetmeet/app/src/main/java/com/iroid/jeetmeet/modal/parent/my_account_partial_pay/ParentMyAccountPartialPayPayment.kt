package com.iroid.jeetmeet.modal.parent.my_account_partial_pay

data class ParentMyAccountPartialPayPayment(
    val advance_deduction: String,
    val amount: String,
    val discount: String,
    val paid_amount: String,
    val pending_amount: Int,
    val transaction_id: String
)