package com.proteinium.proteiniumdietapp.ui.main.home.home.notification

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.data.api.ApiRepositoryProvider
import com.proteinium.proteiniumdietapp.pojo.CommonResponse
import com.proteinium.proteiniumdietapp.pojo.notifications.NotificationsResponse
import com.proteinium.proteiniumdietapp.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class NotificationViewModel:ViewModel() {

    private val notificationResponseLiveData = MutableLiveData<Resource<NotificationsResponse>>()
    private val updateNotificationStatusResponseLiveData = MutableLiveData<Resource<CommonResponse>>()

    fun fetchNotifications(user_id: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            notificationResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.getNotifications(user_id).let {
                    val response = it.body()
                    if (response?.status!!) {
                        notificationResponseLiveData.postValue(Resource.success(response))
                    } else {
                        notificationResponseLiveData.postValue(Resource.error(response.message, response))
                    }
                }
            } catch (ex: NetworkErrorException) {
                notificationResponseLiveData.postValue(
                    Resource.noInternet(
                        R.string.no_internet.toString(),
                        null
                    )
                )

            } catch (ex: UnknownHostException) {
                notificationResponseLiveData.postValue(
                    Resource.noInternet(
                        R.string.no_internet.toString(),
                        null
                    )
                )

            } catch (ex: Exception) {
                notificationResponseLiveData.postValue(
                    Resource.error(
                        R.string.something_wrong.toString(),
                        null
                    )
                )
            }

        }
    }

    fun getNotificationsResponse(): LiveData<Resource<NotificationsResponse>> {
        return notificationResponseLiveData
    }

    fun updateNotificationStatus(notification_id: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            updateNotificationStatusResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.updateNotificationStatus(notification_id).let {
                    val response = it.body()
                    if (response?.status!!) {
                        updateNotificationStatusResponseLiveData.postValue(Resource.success(response))
                    } else {
                        updateNotificationStatusResponseLiveData.postValue(Resource.error(response.message, response))
                    }
                }
            } catch (ex: NetworkErrorException) {
                updateNotificationStatusResponseLiveData.postValue(
                    Resource.noInternet(
                        R.string.no_internet.toString(),
                        null
                    )
                )

            } catch (ex: UnknownHostException) {
                updateNotificationStatusResponseLiveData.postValue(
                    Resource.noInternet(
                        R.string.no_internet.toString(),
                        null
                    )
                )

            } catch (ex: Exception) {
                updateNotificationStatusResponseLiveData.postValue(
                    Resource.error(
                        R.string.something_wrong.toString(),
                        null
                    )
                )
            }

        }
    }

    fun getUpdateNotificationStatusResponse(): LiveData<Resource<CommonResponse>> {
        return updateNotificationStatusResponseLiveData
    }
}