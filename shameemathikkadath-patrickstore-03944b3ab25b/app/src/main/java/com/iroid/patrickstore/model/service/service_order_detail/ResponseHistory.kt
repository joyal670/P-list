package com.iroid.patrickstore.model.service.service_order_detail

data class ResponseHistory(
    val _id: String,
    val comments: String,
    val date: String,
    val isCustomer: Boolean,
    val quantity: Int,
    val quoteStatus: String,
    val rate: String,
    val time: String,
    val tsCreatedAt: Long
)
