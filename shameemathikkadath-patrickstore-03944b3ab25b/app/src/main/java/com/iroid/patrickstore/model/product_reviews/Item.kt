package com.iroid.patrickstore.model.product_reviews

data class Item(
    val __v: Int,
    val customerId: String,
    val id: String,
    val productId: ProductId,
    val rating: Float,
    val review: String,
    val reviewTitle: String
)
