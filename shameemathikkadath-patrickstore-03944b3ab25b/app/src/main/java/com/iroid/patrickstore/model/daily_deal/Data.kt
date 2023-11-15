package com.iroid.patrickstore.model.daily_deal

data class Data(
    val hasNextPage: Boolean,
    val items: List<DailyDeal>,
    val perPage: Int,
    val totalCount: Int,
    val totalPages: Int
)
