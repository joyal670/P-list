package com.iroid.patrickstore.model.order_detail

import com.iroid.patrickstore.model.add_address.Address

data class OrderDetail(
    val __v: Int,
    val _id: String,
    val billingAddressId: String,
    val cartItems: List<CartItem>,
    val createdAt: String,
    val dateOfPurchase: String,
    val deliveryCharge: Int,
    val deliverySurgeCharge: Int,
    val franchiseId: String,
    val grandTotal: Double,
    val isAnyDeliverySurge: Boolean,
    val isConvertedToOrder: Boolean,
    val itemTotal: Double,
    val orderHistory: List<Any>,
    val orderId: Int,
    val orderStatus: String,
    val packingCharge: Int,
    val paymentMethod: String,
    val sellerIds: List<Any>,
    val sellerLocations: List<Any>,
    val serviceCharge: Int,
    val shippingAddressId: Address,
    val taxAmount: Double,
    val tip: Int,
    val totalAdminCommission: Double,
    val totalFranchiseCommission: Any,
    val totalPrice: Double,
    val updatedAt: String,
    val user: String
)
