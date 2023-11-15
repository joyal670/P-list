package com.iroid.patrickstore.model.cashback

data class Data(
    val cashbackAmount: Double,
    val hasNextPage: Boolean,
    val items: List<Item>,
    val perPage: Int,
    val totalCount: Int,
    val totalPages: Int
)
