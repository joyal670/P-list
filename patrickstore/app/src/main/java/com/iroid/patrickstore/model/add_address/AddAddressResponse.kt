package com.iroid.patrickstore.model.add_address

data class AddAddressResponse(
    val `data`: Address,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)