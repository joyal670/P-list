package com.iroid.jeetmeet.modal.parent.my_account_debit_from_advance

data class ParentMyaccountDebitFromAdvanceResponse(
    val fee_details: List<ParentMyaccountDebitFromAdvanceFeeDetail>,
    val payment: ParentMyaccountDebitFromAdvancePayment,
    val settings: ParentMyaccountDebitFromAdvanceSettings,
    val status: Int,
    val student: ParentMyaccountDebitFromAdvanceStudent,
    val data: String
)