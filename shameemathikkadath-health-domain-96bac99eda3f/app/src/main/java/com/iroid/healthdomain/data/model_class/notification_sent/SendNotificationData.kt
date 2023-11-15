package com.iroid.healthdomain.data.model_class.notification_sent

data class SendNotificationData(
    val challenge: Challenge,
    val challenge_id: Int,
    val created_at: String,
    val extra1: Any,
    val from: Int,
    val id: Int,
    val to: Int,
    val updated_at: String,
    val user_from: UserFrom,
    val user_to: UserTo
)