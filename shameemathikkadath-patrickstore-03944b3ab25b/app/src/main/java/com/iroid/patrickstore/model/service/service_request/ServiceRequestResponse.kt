package com.iroid.patrickstore.model.service.service_request

data class ServiceRequestResponse(
    val `data`: ServiceRequest,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)
