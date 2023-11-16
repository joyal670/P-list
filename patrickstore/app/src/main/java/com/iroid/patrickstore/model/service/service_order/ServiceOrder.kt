package com.iroid.patrickstore.model.service.service_order

data class ServiceOrder(
    val hasNextPage: Boolean,
    val items: List<ItemServiceOrder>,
    val perPage: Int,
    val totalCount: Int,
    val totalPages: Int
)
