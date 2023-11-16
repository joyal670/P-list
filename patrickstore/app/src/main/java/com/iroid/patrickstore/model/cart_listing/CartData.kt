package com.iroid.patrickstore.model.cart_listing

data class CartData(
    val cartItems: CartItems,
    val deliveryCharge: Any,
    val deliverySurgeCharge: Any,
    val grandTotal: Double,
    val isAnyDeliverySurge: Boolean,
    val itemTotal: Any,
    val packingCharge: Any,
    val serviceCharge: Any,
    val taxAmount: Double
)