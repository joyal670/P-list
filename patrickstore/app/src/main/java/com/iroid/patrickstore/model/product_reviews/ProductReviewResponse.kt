package com.iroid.patrickstore.model.product_reviews

data class ProductReviewResponse(
    val `data`: Data,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)
