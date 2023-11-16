package com.property.propertyagent.agent_panel.ui.main.home.myclient.users.completed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.agent.agent_user_booking_property_completed.AgentUserBookingPropertyCompletedResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class UserCompletedViewModel : ViewModel() {
    private val agentUserBookingPropertyCompletedResponseLiveData =
        MutableLiveData<Resource<AgentUserBookingPropertyCompletedResponse>>()

    fun agentUserBookingPropertyCompletedList(page: String) {
        val params = mapOf(
            "page" to page.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "completed" to "1".toRequestBody("multipart/form-data".toMediaTypeOrNull())
        )
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentUserBookingPropertyCompletedResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentUserBookingPropertyCompletedList(params).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentUserBookingPropertyCompletedResponseLiveData.postValue(
                            Resource.success(
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentUserBookingPropertyCompletedResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentUserBookingPropertyCompletedResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentUserBookingPropertyCompletedResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }

            } catch (ex: Exception) {
                agentUserBookingPropertyCompletedResponseLiveData.postValue(
                    Resource.noInternet(
                        "",
                        null
                    )
                )
            }
        }
    }

    fun getAgentUserBookingPropertyCompletedResponse(): LiveData<Resource<AgentUserBookingPropertyCompletedResponse>> {
        return agentUserBookingPropertyCompletedResponseLiveData
    }
}