package com.property.propertyuser.modal.rental_user_property_view_details

data class UserProperty(
    val cancel_status: Int,
    val check_in: String,
    val check_out: String,
    val document: String,
    val document_verified: Int,
    val id: Int,
    val property_id: Int,
    val status: Int,
    val type: Int,
    val due_date : String,
    val contract_rel : ContractRel,
    val user_property_related: UserPropertyRelated
)