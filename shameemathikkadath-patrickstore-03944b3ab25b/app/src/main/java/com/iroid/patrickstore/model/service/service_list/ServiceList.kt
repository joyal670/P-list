package com.iroid.patrickstore.model.service.service_list

data class ServiceList(
    val hasNextPage: Boolean,
    val items: List<Item>,
    val perPage: Int,
    val totalCount: Int,
    val totalPages: Int
)
