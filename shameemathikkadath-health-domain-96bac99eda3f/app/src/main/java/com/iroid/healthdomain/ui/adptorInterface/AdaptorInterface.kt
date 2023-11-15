package com.iroid.healthdomain.ui.adptorInterface

import com.iroid.healthdomain.data.model_class.contacts_api.ContactData

interface AdaptorInterface {
    fun onItemClick(contactModel: ContactData)

    fun favorite(id:Int,following:Int)

    fun refreshAllContact()

}