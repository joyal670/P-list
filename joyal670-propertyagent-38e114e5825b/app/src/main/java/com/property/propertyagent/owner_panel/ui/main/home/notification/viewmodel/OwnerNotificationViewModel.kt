package com.property.propertyagent.owner_panel.ui.main.home.notification.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.owner.owner_notification.OwnerNotificationResponse
import com.property.propertyagent.modal.owner.owner_notification_update.OwnerNotificationStatusUpdateResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch

class OwnerNotificationViewModel : ViewModel() {

    private val ownerNotificationResponseLiveData =
        MutableLiveData<Resource<OwnerNotificationResponse>>()

    private val ownerNotificationUpdateResponseLiveData =
        MutableLiveData<Resource<OwnerNotificationStatusUpdateResponse>>()

    /* notification listing */
    fun notification(page : String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            ownerNotificationResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.ownerNotification(page).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        ownerNotificationResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        ownerNotificationResponseLiveData.postValue(Resource.error("null" ,
                            response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        ownerNotificationResponseLiveData.postValue(Resource.dataEmpty("null" ,
                            response))
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        ownerNotificationResponseLiveData.postValue(Resource.dataEmpty("null" ,
                            response))
                    }
                }

            } catch (ex : Exception) {
                ownerNotificationResponseLiveData.postValue(Resource.noInternet("" , null))
                Log.e("TAG" , "paymentListOfProperties: $ex")
            }
        }
    }

    fun getOwnerNotificationResponse() : LiveData<Resource<OwnerNotificationResponse>> {
        return ownerNotificationResponseLiveData
    }

    /* notification status update */
    fun notificationUpdate(notification_id : String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            ownerNotificationUpdateResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.ownerNotificationStatusUpdate(notification_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        ownerNotificationUpdateResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        ownerNotificationUpdateResponseLiveData.postValue(Resource.error("null" ,
                            response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        ownerNotificationUpdateResponseLiveData.postValue(Resource.dataEmpty("null" ,
                            response))
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        ownerNotificationUpdateResponseLiveData.postValue(Resource.dataEmpty("null" ,
                            response))
                    }
                }

            } catch (ex : Exception) {
                ownerNotificationResponseLiveData.postValue(Resource.noInternet("" , null))
                Log.e("TAG" , "paymentListOfProperties: $ex")
            }
        }
    }

    fun getOwnerNotificationUpdateResponse() : LiveData<Resource<OwnerNotificationStatusUpdateResponse>> {
        return ownerNotificationUpdateResponseLiveData
    }

}