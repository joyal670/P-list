package com.proteinium.proteiniumdietapp.pojo.blocks

data class BlockResponse(
    val code: Int,
    val `data`: List<Block>,
    val message: String,
    val status: Boolean
)