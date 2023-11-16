package com.iroid.patrickstore.model.edit_address

data class EditAddressResponse(
    val `data`: Data,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)