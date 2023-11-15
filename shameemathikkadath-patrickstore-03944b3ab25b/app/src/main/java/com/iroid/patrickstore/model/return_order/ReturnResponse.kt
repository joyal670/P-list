package com.iroid.patrickstore.model.return_order

data class ReturnResponse(
    val `data`: Any,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)
