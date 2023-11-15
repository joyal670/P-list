package com.iroid.patrickstore.model.cashback

data class Item(
    val __v: Int,
    val _id: String,
    val cashBackAmount: Double,
    val createdAt: String,
    val customerId: String,
    val isEarnedCashBack: Boolean,
    val orderId: OrderId,
    val status: Int,
    val type: String,
    val updatedAt: String
)
