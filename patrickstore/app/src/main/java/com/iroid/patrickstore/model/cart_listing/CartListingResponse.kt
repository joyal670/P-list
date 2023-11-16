package com.iroid.patrickstore.model.cart_listing

data class CartListingResponse(
    val `data`: CartData,
    val error: Boolean,
    val msg: String,
    val statusCode: Int,
    val total: Double
)