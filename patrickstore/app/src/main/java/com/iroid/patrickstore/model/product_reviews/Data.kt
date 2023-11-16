package com.iroid.patrickstore.model.product_reviews

data class Data(
    val hasNextPage: Boolean,
    val items: List<Item>,
    val perPage: Int,
    val totalCount: Int,
    val totalPages: Int
)
