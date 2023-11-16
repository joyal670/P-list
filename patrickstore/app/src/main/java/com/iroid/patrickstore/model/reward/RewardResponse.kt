package com.iroid.patrickstore.model.reward

data class RewardResponse(
    val `data`: Reward,
    val error: Boolean,
    val msg: String,
    val statusCode: Int
)
