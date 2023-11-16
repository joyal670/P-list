package com.iroid.patrickstore.model.remove_single_item_from_cart

data class Data(
    val __v: Int,
    val _id: String,
    val cartItems: List<Any>,
    val createdAt: String,
    val franchiseId: String,
    val isConvertedToOrder: Boolean,
    val orderHistory: List<Any>,
    val sellerIds: List<Any>,
    val sellerLocations: List<Any>,
    val updatedAt: String,
    val user: String
)