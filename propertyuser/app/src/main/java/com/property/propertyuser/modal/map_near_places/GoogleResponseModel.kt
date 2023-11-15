package com.property.propertyuser.modal.map_near_places

data class GoogleResponseModel(
    val html_attributions: List<Any>,
    val info_messages: List<Any>,
    val results: List<Result>,
    val status: String
)