package com.iroid.patrickstore.model.categoryProdcut.seller

import com.iroid.patrickstore.model.seller.SingleSeller

data class SellerData(
    val hasNextPage: Boolean,
    val items: List<SingleSeller>,
    val perPage: Int,
    val totalCount: Int,
    val totalPages: Int
)