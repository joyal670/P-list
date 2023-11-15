package com.property.propertyuser.ui.main.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.ReadNotificationResponse
import com.property.propertyuser.modal.events_list.EventListResponse
import com.property.propertyuser.modal.user_notification.NotificationResponse
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Resource
import kotlinx.coroutines.launch

class NotificationViewModel:ViewModel() {
    private val notificationResponseLiveData= MutableLiveData<Resource<NotificationResponse>>()

    fun fetchNotification(page:String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            notificationResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.notification(page).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        notificationResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        notificationResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        notificationResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status==Constants.INTERNAL_SERVER_ERROR){
                        notificationResponseLiveData.postValue(Resource.error("null",response))
                    }
                }

            }catch (ex:Exception){
                notificationResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getNotificationResponse(): LiveData<Resource<NotificationResponse>> {
        return notificationResponseLiveData
    }

    private val readNotificationResponseLiveData= MutableLiveData<Resource<ReadNotificationResponse>>()

    fun fetchReadNotificationUpdate(notification_id:String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            readNotificationResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.readNotificationUpdate(notification_id).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        readNotificationResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        readNotificationResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        readNotificationResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                readNotificationResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getReadNotificationUpdateResponse(): LiveData<Resource<ReadNotificationResponse>> {
        return readNotificationResponseLiveData
    }
}