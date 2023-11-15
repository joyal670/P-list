package com.proteinium.proteiniumdietapp.pojo.notifications

data class Notification(
    val id: Int,
    val message: String,
    var seen_status: Int,
    val title: String,
    val created_at:String,
    val updated_at:String
)