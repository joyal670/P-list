package com.proteinium.proteiniumdietapp.pojo.dislike_tags

data class TagsResponse(
    val code: Int,
    val `data`: TagsData,
    val message: String,
    val status: Boolean
)