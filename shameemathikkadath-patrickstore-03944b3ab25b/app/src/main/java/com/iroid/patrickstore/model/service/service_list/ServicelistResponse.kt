package com.iroid.patrickstore.model.service.service_list

data class ServicelistResponse(
    val `data`: ServiceList,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)
