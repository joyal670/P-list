package com.proteinium.proteiniumdietapp.pojo.payment

data class InvoiceTransaction(
    val AuthorizationId: String,
    val CardNumber: Any,
    val Currency: String,
    val CustomerServiceCharge: String,
    val DueValue: String,
    val Error: Any,
    val PaidCurrency: String,
    val PaidCurrencyValue: String,
    val PaymentGateway: String,
    val PaymentId: String,
    val ReferenceId: String,
    val TrackId: String,
    val TransactionDate: String,
    val TransactionId: String,
    val TransactionStatus: String,
    val TransationValue: String
)