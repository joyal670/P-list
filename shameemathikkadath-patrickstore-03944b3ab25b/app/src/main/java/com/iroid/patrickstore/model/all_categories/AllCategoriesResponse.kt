package com.iroid.patrickstore.model.all_categories

data class AllCategoriesResponse(
    val `data`: List<AllCategories>,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)