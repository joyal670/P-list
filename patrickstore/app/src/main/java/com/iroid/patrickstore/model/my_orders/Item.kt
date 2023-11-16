package com.iroid.patrickstore.model.my_orders

import com.iroid.patrickstore.model.CartItem
import com.iroid.patrickstore.model.CartItem2

data class Item(
    val _id: String,
    val dateOfPurchase: String,
    val itemCount: Int,
    val orderId: Int,
    val cartItems: List<CartItem2>
)
