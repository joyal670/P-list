package com.proteinium.proteiniumdietapp.pojo.get_addresses

data class Address(
    val id:Int,
    val appartment: String,
    val area: String,
    val avenue: String,
    val block: String,
    val building: Any,
    var default: Int,
    val floor: String,
    val governorate: String,
    val street: String,
    val title: Any,
    val additonal_directions:String
)
