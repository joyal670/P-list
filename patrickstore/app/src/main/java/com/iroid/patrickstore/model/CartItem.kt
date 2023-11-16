package com.iroid.patrickstore.model

data class

CartItem(
    val _id: String,
    val adminCommission: Double,
    val franchiseCommission: Int,
    val orderStatus: String,
    val price: Double,
    val product: Product,
    val quantity: Int,
    val sellerId: String
)