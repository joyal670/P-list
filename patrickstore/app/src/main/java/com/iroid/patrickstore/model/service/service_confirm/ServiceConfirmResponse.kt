package com.iroid.patrickstore.model.service.service_confirm

data class ServiceConfirmResponse(
    val `data`: ServiceConfirm,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)
