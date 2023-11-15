package com.iroid.patrickstore.model.single_product

import com.iroid.patrickstore.model.product.Product

data class SingleData(
    val product: SingleProduct,
    val similarProducts: List<Product>
)