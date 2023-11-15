package com.iroid.healthdomain.ui.home.my_health

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.iroid.healthdomain.data.model_class.activity_api.UpdateActivityResponse
import com.iroid.healthdomain.data.model_class.index.IndexApiResponse
import com.iroid.healthdomain.data.model_class.update_challenge.UpdateChallengeResponse
import com.iroid.healthdomain.data.model_class.updated_steps_data.SendStepsUpdates
import com.iroid.healthdomain.data.model_class.user_profile.UserModelResponse
import com.iroid.healthdomain.data.network.Resource
import com.iroid.healthdomain.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class MyHealthViewModel(val repository: MyHealthRepository, application: Application) : BaseViewModel(application) {

    val indexApiResponse: LiveData<Resource<IndexApiResponse>> = MutableLiveData()
    val userApiResponse: LiveData<Resource<UserModelResponse>> = MutableLiveData()
    val updateActivityResponse: LiveData<Resource<UpdateActivityResponse>> = MutableLiveData()
    val updateStepsActivityResponse: LiveData<Resource<UpdateActivityResponse>> = MutableLiveData()

    val updateChallengeApiResponse: LiveData<Resource<UpdateChallengeResponse>> = MutableLiveData()

    fun getIndexApi() = viewModelScope.launch {
        indexApiResponse as MutableLiveData
        // setLoading(true)
        indexApiResponse.value = Resource.Loading
        indexApiResponse.value = repository.requestIndexApi()
        // setLoading(false)
    }

    fun getUserProfile() = viewModelScope.launch {
        userApiResponse as MutableLiveData
        userApiResponse.value = Resource.Loading
        userApiResponse.value = repository.requestUserProfile()
    }

    fun sendStepsToBackend(steps: String) = viewModelScope.launch {
        updateActivityResponse as MutableLiveData
        updateActivityResponse.value = Resource.Loading
        updateActivityResponse.value = repository.sendSteps(steps)
    }

    fun sendSteps(steps: SendStepsUpdates) = viewModelScope.launch {
        updateStepsActivityResponse as MutableLiveData
        updateStepsActivityResponse.value = Resource.Loading
        updateStepsActivityResponse.value = repository.sendStepsUpdated(steps)
    }

/*    fun sendSteps(steps: String,date:String) = viewModelScope.launch {
        try {
            updateStepsActivityResponse as MutableLiveData
            updateStepsActivityResponse.value = Resource.Loading

            updateStepsActivityResponse.value = repository.sendStepsUpdated(steps,date)
        }catch (ex:Exception){
            ex.printStackTrace()

        }
    }*/

    fun updateChallenge(id:String,status:String) = viewModelScope.launch {
        updateChallengeApiResponse as MutableLiveData
        updateChallengeApiResponse.value = Resource.Loading
        updateChallengeApiResponse.value = repository.updateChallenge(id,status)
    }

    suspend fun setNotificationStatus(value: Boolean) {
        repository.saveNotification(value)
    }
    suspend fun setHdStatus(value: String) {
        repository.saveHdStatus(value)
    }
}