package com.iroid.patrickstore.model.wishlist_listing

data class WishListResponse(
    val `data`: Data,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)