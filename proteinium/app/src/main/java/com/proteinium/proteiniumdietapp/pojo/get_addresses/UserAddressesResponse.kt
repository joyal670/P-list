package com.proteinium.proteiniumdietapp.pojo.get_addresses

data class UserAddressesResponse(
    val code: Int,
    val `data`: List<Address>,
    val message: String,
    val status: Boolean
)