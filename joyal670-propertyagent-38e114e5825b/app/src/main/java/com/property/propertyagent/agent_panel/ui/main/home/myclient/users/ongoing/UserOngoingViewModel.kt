package com.property.propertyagent.agent_panel.ui.main.home.myclient.users.ongoing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.agent.agent_user_booking_property_ongoing.AgentUserBookingPropertyOngoingResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch

class UserOngoingViewModel : ViewModel() {
    private val agentUserBookingPropertyOngoingResponseLiveData =
        MutableLiveData<Resource<AgentUserBookingPropertyOngoingResponse>>()

    fun agentUserBookingPropertyOngoingList(page: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentUserBookingPropertyOngoingResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentUserBookingPropertyOngoingList(page).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentUserBookingPropertyOngoingResponseLiveData.postValue(
                            Resource.success(
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentUserBookingPropertyOngoingResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentUserBookingPropertyOngoingResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentUserBookingPropertyOngoingResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentUserBookingPropertyOngoingResponseLiveData.postValue(
                    Resource.noInternet(
                        "",
                        null
                    )
                )
            }
        }
    }

    fun getAgentUserBookingPropertyOngoingResponse(): LiveData<Resource<AgentUserBookingPropertyOngoingResponse>> {
        return agentUserBookingPropertyOngoingResponseLiveData
    }
}