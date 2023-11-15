package com.property.propertyuser.ui.main.side_menu.rewards

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.user_notification.NotificationResponse
import com.property.propertyuser.modal.user_rewards.UserRewaredResponse
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Resource
import kotlinx.coroutines.launch

class RewardsViewModel:ViewModel() {
    private val rewardsResponseLiveData= MutableLiveData<Resource<UserRewaredResponse>>()

    fun fetchRewards(){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            rewardsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.userRewards().let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        rewardsResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        rewardsResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        rewardsResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                rewardsResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getRewardsResponse(): LiveData<Resource<UserRewaredResponse>> {
        return rewardsResponseLiveData
    }
}