package com.iroid.patrickstore.model.order_offer

data class OrderOffer(
    val cashbackAmount: Any,
    val isCashbackExists: Boolean,
    val isRewardExists: Boolean,
    val maximumCashBack: Any,
    val rewardPointPercentage: Int
)
