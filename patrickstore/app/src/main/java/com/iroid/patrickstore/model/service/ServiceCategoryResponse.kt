package com.iroid.patrickstore.model.service

data class ServiceCategoryResponse(
    val `data`: List<ServiceCategory>,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)