package com.iroid.patrickstore.model.addresslist

data class Data(
    val hasNextPage: Boolean,
    val items: List<Item>,
    val perPage: Int,
    val totalCount: Int,
    val totalPages: Int
)