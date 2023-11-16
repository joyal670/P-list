package com.iroid.patrickstore.model.festval_offers

data class Product(
    val __v: Int,
    val category: String,
    val id: String,
    val mainImage: MainImageX,
    val otherImage: List<Any>,
    val product: List<ProductX>,
    val sellerId: SellerId,
    val title: String,
    val type: String
)
