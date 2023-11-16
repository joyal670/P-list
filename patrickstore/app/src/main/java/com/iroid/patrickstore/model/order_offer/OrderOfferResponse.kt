package com.iroid.patrickstore.model.order_offer

data class OrderOfferResponse(
    val `data`: OrderOffer,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)
