package com.iroid.patrickstore.model.festval_offers

data class FestivalOfferResponse(
    val `data`: Data,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)
