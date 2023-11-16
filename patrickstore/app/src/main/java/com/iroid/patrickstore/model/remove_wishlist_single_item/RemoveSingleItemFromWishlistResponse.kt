package com.iroid.patrickstore.model.remove_wishlist_single_item

data class RemoveSingleItemFromWishlistResponse(
    val `data`: Data,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)