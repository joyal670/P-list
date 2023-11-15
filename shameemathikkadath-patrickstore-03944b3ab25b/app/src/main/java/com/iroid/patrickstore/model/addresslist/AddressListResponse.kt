package com.iroid.patrickstore.model.addresslist

data class AddressListResponse(
    val `data`: Data,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)