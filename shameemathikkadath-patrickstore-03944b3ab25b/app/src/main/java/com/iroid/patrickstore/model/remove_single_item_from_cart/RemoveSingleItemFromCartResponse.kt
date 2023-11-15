package com.iroid.patrickstore.model.remove_single_item_from_cart

data class RemoveSingleItemFromCartResponse(
    val `data`: Data,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)