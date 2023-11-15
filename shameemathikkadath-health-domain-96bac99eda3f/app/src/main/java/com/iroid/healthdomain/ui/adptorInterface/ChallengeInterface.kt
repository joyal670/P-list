package com.iroid.healthdomain.ui.adptorInterface

import com.iroid.healthdomain.data.model_class.contacts_api.ContactData

interface ChallengeInterface {
    fun onLoadChallenge(contactModel: ContactData)
}