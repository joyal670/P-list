package com.iroid.patrickstore.model.service.service_order

data class ResponseHistory(
    val _id: String,
    val comments: String,
    val date: String,
    val isCustomer: Boolean,
    val quantity: Int,
    val quoteStatus: String,
    val rate: Int,
    val time: String,
    val tsCreatedAt: Long
)
