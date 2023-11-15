package com.property.propertyuser.modal.my_property_list

data class BookedProperty(
    val id: Int,
    val property_priority_image: PropertyPriorityImage,
    val property_name:String,
    val property_reg_no: String,
    val rent:String,
    val property_to: Int,
    val selling_price: String,
    val user_property_related: UserPropertyRelated,
    val due_date: String,
    val status: Int
)