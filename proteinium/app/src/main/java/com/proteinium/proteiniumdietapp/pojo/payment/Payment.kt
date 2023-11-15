package com.proteinium.proteiniumdietapp.pojo.payment

data class Payment(
    val Comments: Any,
    val CreatedDate: String,
    val CustomerEmail: Any,
    val CustomerMobile: String,
    val CustomerName: String,
    val CustomerReference: Any,
    val ExpiryDate: String,
    val InvoiceDisplayValue: String,
    val InvoiceId: Int,
    val InvoiceItems: List<Any>,
    val InvoiceReference: String,
    val InvoiceStatus: String,
    val InvoiceTransactions: List<InvoiceTransaction>,
    val InvoiceValue: Double,
    val Suppliers: List<Any>,
    val UserDefinedField: Any
)