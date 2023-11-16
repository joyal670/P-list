package com.iroid.healthdomain.data.model_class.match_contact

import com.iroid.healthdomain.data.model_class.contacts_api.ContactData

data class GetContactMatchResponse(
    val `data`: List<ContactData>,
    val message: String,
    val status: Int
)