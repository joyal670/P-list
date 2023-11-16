package com.iroid.patrickstore.model.location_request.response

data class LocationResponse(
    val `data`: Data,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)