package com.iroid.patrickstore.model.festval_offers

data class Seller(
    val __v: Int,
    val category: String,
    val id: String,
    val mainImage: MainImageXX,
    val otherImage: List<Any>,
    val product: List<Any>,
    val sellerId: SellerIdX,
    val title: String,
    val type: String
)
