package com.iroid.patrickstore.model.categoryProdcut.seller

data class SellerCategoryWiseResponse(
    val `data`: SellerData,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)