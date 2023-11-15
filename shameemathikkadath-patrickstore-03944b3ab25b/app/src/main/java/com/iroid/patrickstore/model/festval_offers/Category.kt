package com.iroid.patrickstore.model.festval_offers

data class Category(
    val __v: Int,
    val category: String,
    val categoryId: String,
    val id: String,
    val mainImage: MainImage,
    val otherImage: List<Any>,
    val product: List<Any>,
    val title: String,
    val type: String
)
