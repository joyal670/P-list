package com.iroid.patrickstore.model.single_address

data class SingleAddressResponse(
    val `data`: Data,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)