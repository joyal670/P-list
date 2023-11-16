package com.property.propertyagent.agent_panel.ui.main.sidemenu.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.agent.agent_notification_list.AgentNotificationActionResponse
import com.property.propertyagent.modal.agent.agent_notification_list.AgentNotificationListResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch

class NotificationViewModel : ViewModel() {

    private val agentNotificationResponseLiveData =
        MutableLiveData<Resource<AgentNotificationListResponse>>()

    private val agentNotificationActionResponseLiveData =
        MutableLiveData<Resource<AgentNotificationActionResponse>>()

    fun agentNotificationList(page: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentNotificationResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentNotificationList(page).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentNotificationResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentNotificationResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentNotificationResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentNotificationResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentNotificationResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getAgentNotificationListResponse(): LiveData<Resource<AgentNotificationListResponse>> {
        return agentNotificationResponseLiveData
    }

    fun agentNotificationAction(notification_id: String, status: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentNotificationActionResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentNotificationAction(notification_id, status).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentNotificationActionResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentNotificationActionResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentNotificationActionResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentNotificationActionResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentNotificationActionResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getAgentNotificationActionResponse(): LiveData<Resource<AgentNotificationActionResponse>> {
        return agentNotificationActionResponseLiveData
    }
}