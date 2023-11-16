package com.iroid.patrickstore.model.service.service_order

data class ItemServiceOrder(
    val __v: Int,
    val customerId: String,
    val customerMsg: String,
    val date: String,
    val franchiseId: String,
    val id: String,
    val isCustomerNegotiated: Boolean,
    val isSellerNegotiated: Boolean,
    val quantity: Int,
    val rate: Int,
    val requeststatus: String,
    val responseHistory: List<ResponseHistory>,
    val sellerId: SellerId,
    val serviceId: ServiceId,
    val time: String,
    val isConvertedToOrder:Boolean
)
