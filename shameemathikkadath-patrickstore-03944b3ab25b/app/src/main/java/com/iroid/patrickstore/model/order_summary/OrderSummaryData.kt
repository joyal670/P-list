package com.iroid.patrickstore.model.order_summary

import com.iroid.patrickstore.model.add_address.Address

data class OrderSummaryData(
    val cartItems: CartItems,
    val deliveryCharge: Any,
    val deliverySurgeCharge: Any,
    val grandTotal: Double,
    val isAnyDeliverySurge: Boolean,
    val itemTotal: Double,
    val packingCharge: Any,
    val serviceCharge: Any,
    val taxAmount: Any,
    val addressData: Address,
    val cashbackAmount:Double,
    val offerAmount:Double,
    val totalOfferAmount:Double,
    val offerId:String



)
