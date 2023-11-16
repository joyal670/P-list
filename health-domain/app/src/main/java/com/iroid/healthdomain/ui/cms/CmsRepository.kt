package com.iroid.healthdomain.ui.cms

import com.iroid.healthdomain.data.dummyModel.HashedModel
import com.iroid.healthdomain.data.network.ApiServices
import com.iroid.healthdomain.data.user_preferences.UserPreferences
import com.iroid.healthdomain.ui.base.BaseRepository

class CmsRepository(
    private val api: ApiServices,
    private val userPreferences: UserPreferences
) : BaseRepository() {

    suspend fun getTerms() = safeApiCall {
        api.getTerms()
    }

    suspend fun getPrivacyPolicy() = safeApiCall {
        api.getPrivacy()

    }


}