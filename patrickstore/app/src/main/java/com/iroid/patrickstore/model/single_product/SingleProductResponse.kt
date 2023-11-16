package com.iroid.patrickstore.model.single_product

data class SingleProductResponse(
    val `data`: SingleData,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)