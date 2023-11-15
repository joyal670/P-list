package com.proteinium.proteiniumdietapp.pojo.edit_address

data class EditSingleAddressResponse(
    val code: Int,
    val `data`: EditSingleAddress,
    val message: String,
    val status: Boolean
)