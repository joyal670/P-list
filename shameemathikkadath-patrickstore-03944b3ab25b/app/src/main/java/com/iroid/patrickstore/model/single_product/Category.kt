package com.iroid.patrickstore.model.single_product

data class Category(
    val _id: String,
    val isPerishable: Boolean,
    val name: String,
    val uniqueName: String
)