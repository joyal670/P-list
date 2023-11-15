package com.iroid.patrickstore.model.cart_listing

import com.iroid.patrickstore.model.product.Product

data class CartItem(
    val _id: String,
    val adminCommission: Double,
    val franchiseCommission: Any,
    val orderStatus: String,
    val price: Double,
    val product: Product,
    val quantity: Int,
    val sellerId: SellerId
)