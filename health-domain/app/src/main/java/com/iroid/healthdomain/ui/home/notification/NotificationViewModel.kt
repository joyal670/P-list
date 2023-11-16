package com.iroid.healthdomain.ui.home.notification

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.iroid.healthdomain.data.model_class.contacts_api.ContactResponse
import com.iroid.healthdomain.data.model_class.notification_receive.NotificationReceiveResponse
import com.iroid.healthdomain.data.model_class.notification_sent.NotificationSentResponse
import com.iroid.healthdomain.data.network.Resource
import com.iroid.healthdomain.ui.base.BaseViewModel
import com.iroid.healthdomain.ui.home.mainActivity.HomeRepository
import kotlinx.coroutines.launch

class NotificationViewModel (private val repository: HomeRepository, application: Application) : BaseViewModel(application) {

    val receiveNotificationLiveData: LiveData<Resource<NotificationReceiveResponse>> = MutableLiveData()
    val sentNotificationLiveData: LiveData<Resource<NotificationSentResponse>> = MutableLiveData()

    fun getReceivedNotifications() = viewModelScope.launch {
        receiveNotificationLiveData as MutableLiveData
        receiveNotificationLiveData.value = Resource.Loading
        receiveNotificationLiveData.value = repository.getReceivedNotification()
    }

    fun getSentNotifications() = viewModelScope.launch {
        sentNotificationLiveData as MutableLiveData
        sentNotificationLiveData.value = Resource.Loading
        sentNotificationLiveData.value = repository.getSentNotification()
    }

}