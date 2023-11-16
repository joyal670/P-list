package com.iroid.healthdomain.ui.home.mainActivity

import com.iroid.healthdomain.data.dummyModel.HashedModel
import com.iroid.healthdomain.data.network.ApiServices
import com.iroid.healthdomain.data.user_preferences.UserPreferences
import com.iroid.healthdomain.ui.base.BaseRepository

class HomeRepository(
    private val api: ApiServices,
    private val userPreferences: UserPreferences
) : BaseRepository() {

    suspend fun getAllContacts() = safeApiCall {
        api.getContacts()
    }

    suspend fun sendHashedList(hashedModel: HashedModel) = safeApiCall {
        api.GetContactMatch(hashedModel)
    }

    suspend fun sentInvite(number: String) = safeApiCall {
        api.inviteUser(number, "91")
    }

    suspend fun createChallenge(id: String) = safeApiCall {
        api.createChallenge(id, "Challenge")
    }

    suspend fun addRemoveFav(id: String, status: String) = safeApiCall {
        api.addRemoveFavorite(id, status)
    }

    suspend fun getPastChallenges(id: String, page: String) = safeApiCall {
        api.getPastChallenges(id, page)
    }

    suspend fun getReceivedNotification() = safeApiCall {
        api.getReceivedNotification()
    }

    suspend fun getSentNotification() = safeApiCall {
        api.getSentNotification()
    }
}