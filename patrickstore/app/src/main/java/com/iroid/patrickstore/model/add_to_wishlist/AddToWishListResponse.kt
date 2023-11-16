package com.iroid.patrickstore.model.add_to_wishlist

data class AddToWishListResponse(
    val `data`: Data,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)