package com.iroid.healthdomain.data.model_class.notification_sent

data class Data(
    val current_page: Int,
    val `data`: List<SendNotificationData>,
    val first_page_url: String,
    val from: Int,
    val last_page: Int,
    val last_page_url: String,
    val next_page_url: Any,
    val path: String,
    val per_page: Int,
    val prev_page_url: Any,
    val to: Int,
    val total: Int
)