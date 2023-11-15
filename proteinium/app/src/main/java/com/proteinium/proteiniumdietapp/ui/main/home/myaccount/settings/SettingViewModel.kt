package com.proteinium.proteiniumdietapp.ui.main.home.myaccount.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.proteinium.proteiniumdietapp.base.BaseViewModel
import com.proteinium.proteiniumdietapp.data.api.ApiRepositoryProvider
import com.proteinium.proteiniumdietapp.pojo.CommonResponse
import com.proteinium.proteiniumdietapp.pojo.getNotification.GetNotificationResponse
import com.proteinium.proteiniumdietapp.utils.Resource
import kotlinx.coroutines.launch
import java.lang.Exception

class SettingViewModel : BaseViewModel() {
    private val setPushLiveData = MutableLiveData<Resource<CommonResponse>>()
    private val getPushStatusLiveData = MutableLiveData<Resource<GetNotificationResponse>>()

   fun  getPushStatus(user_id: Int){
       val repository=ApiRepositoryProvider.providerApiRepository()
       getPushStatusLiveData.postValue(Resource.loading(null))
       viewModelScope.launch {
           try {
               repository.getPushNotification(user_id).let {
                   val response=it.body()
                   if(response!!.status){
                       getPushStatusLiveData.postValue(Resource.success(response))
                   }else{
                       getPushStatusLiveData.postValue(Resource.error("",response))
                   }
               }
           }catch (ex:Exception){
               getPushStatusLiveData.postValue(Resource.error("", null))
           }
       }
   }
    fun setPush(user_id: Int, notification_status: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        getPushStatusLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {
            try {
                repository.setPushNotification(user_id,notification_status).let {
                    val response = it.body()
                    if (response!!.status) {
                        setPushLiveData.postValue(Resource.success(response))
                    } else {
                        setPushLiveData.postValue(Resource.dataEmpty("", response))
                    }
                }
            } catch (ex: Exception) {
                getPushStatusLiveData.postValue(Resource.error("", null))
            }

        }
    }

    fun getPushLiveData(): LiveData<Resource<CommonResponse>> {
        return setPushLiveData
    }
    fun getPushStatusLiveData(): LiveData<Resource<GetNotificationResponse>> {
        return getPushStatusLiveData
    }
}