package com.property.propertyagent.agent_panel.ui.main.home.myclient.owner.ongoing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.agent.agent_owner_ongoing_list.AgentOwnerOngoingListResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch

class OwnerOngoingViewModel : ViewModel() {

    private val agentOwnerOngoingPropertiesResponseLiveData =
        MutableLiveData<Resource<AgentOwnerOngoingListResponse>>()

    fun agentOwnerOngoingPropertiesListDetails(page: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentOwnerOngoingPropertiesResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentOwnerOngoingPropertiesList(page).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentOwnerOngoingPropertiesResponseLiveData.postValue(
                            Resource.success(
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentOwnerOngoingPropertiesResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentOwnerOngoingPropertiesResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentOwnerOngoingPropertiesResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentOwnerOngoingPropertiesResponseLiveData.postValue(
                    Resource.noInternet(
                        "",
                        null
                    )
                )
            }
        }
    }

    fun getAgentOwnerOngoingPropertiesListDetailsResponse(): LiveData<Resource<AgentOwnerOngoingListResponse>> {
        return agentOwnerOngoingPropertiesResponseLiveData
    }
}