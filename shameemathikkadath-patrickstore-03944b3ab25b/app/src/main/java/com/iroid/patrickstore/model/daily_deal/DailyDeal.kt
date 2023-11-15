package com.iroid.patrickstore.model.daily_deal

import com.iroid.patrickstore.model.product.Product

data class DailyDeal(
    val _id: String,
    val products: List<ProductDailyDeal>,
    val sellerImage: SellerImageX,
    val sellerName: String
)
