package com.iroid.healthdomain.ui.home.my_health

import com.iroid.healthdomain.data.model_class.updated_steps_data.SendStepsUpdates
import com.iroid.healthdomain.data.network.ApiServices
import com.iroid.healthdomain.data.user_preferences.UserPreferences
import com.iroid.healthdomain.ui.base.BaseRepository

class MyHealthRepository(
    private val api: ApiServices,
    private val preferences: UserPreferences? = null
) : BaseRepository() {

    suspend fun requestIndexApi() = safeApiCall {
        api.getIndexApiFromServer()
    }

    suspend fun requestUserProfile() = safeApiCall {
        api.getUserDetails()
    }

    suspend fun sendSteps(steps: String) = safeApiCall {
        api.updateStepCount(steps = steps, activity = "1", calories = "0", duration = "30min")
    }




    suspend fun sendStepsUpdated(steps: SendStepsUpdates) = safeApiCall {
        val passList: ArrayList<SendStepsUpdates> = ArrayList<SendStepsUpdates>()
        passList.add(steps)
        api.updateStepCounts(passList)
    }


    suspend fun updateChallenge(id: String, status: String) = safeApiCall {
        api.updateChallenge(id, status, notes = "")
    }

    suspend fun saveNotification(value: Boolean) {
        preferences?.saveNotification(value)
    }

    suspend fun saveHdStatus(value: String) {
        preferences?.saveHdStatus(value)
    }

}