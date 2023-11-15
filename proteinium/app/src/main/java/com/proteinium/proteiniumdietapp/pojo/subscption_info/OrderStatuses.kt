package com.proteinium.proteiniumdietapp.pojo.subscption_info

data class OrderStatuses(
    val admin_suspended: List<OrderItem>,
    val canceled: List<OrderItem>,
    val completed: List<OrderItem>,
    val pending: List<OrderItem>,
    val user_suspended: List<OrderItem>
)