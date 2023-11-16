package com.iroid.patrickstore.model.cart_listing

data class AddressDetails(
    val address: String,
    val district: String,
    val location: String,
    val pincode: String,
    val state: String
)