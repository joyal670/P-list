package com.iroid.healthdomain.data.model_class.contacts_api

import java.io.Serializable

data class ContactResponse(
        val data: List<ContactData>,
        val message: String,
        val status: Int
):Serializable