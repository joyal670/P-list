package com.iroid.patrickstore.model.service.service_order.confirm_order

data class ConfirmOrder(
    val cashbackAmount: Any,
    val isCashbackExists: Boolean,
    val isRewardExists: Boolean,
    val maximumCashBack: Any,
    val quantity: Int,
    val rate: Int,
    val rewardPointPercentage: Int,
    val sellerServiceCharge: Int,
    val subTotal: Int,
    val subTotalWithServiceCharge: Int
)
