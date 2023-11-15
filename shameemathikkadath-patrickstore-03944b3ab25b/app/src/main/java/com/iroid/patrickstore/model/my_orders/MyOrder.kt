package com.iroid.patrickstore.model.my_orders

data class MyOrder(
    val hasNextPage: Boolean,
    val items: List<Item>,
    val perPage: Int,
    val totalCount: Int,
    val totalPages: Int
)