package com.iroid.patrickstore.model.order_summary

data class CartItems(
    val __v: Int,
    val _id: String,
    val billingAddressId: String,
    val cartItems: List<CartItem>,
    val createdAt: String,
    val franchiseId: String,
    val isConvertedToOrder: Boolean,
    val orderHistory: List<Any>,
//    val sellerIds: List<Any>,
    val sellerLocations: List<Any>,
    val shippingAddressId: String,
    val totalAdminCommission: Double,
    val totalFranchiseCommission: Any,
    val totalPrice: Double,
    val updatedAt: String,
    val user: User
)