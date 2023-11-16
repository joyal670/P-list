package com.property.propertyagent.agent_panel.ui.main.home.myclient.owner.completed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.agent.agent_owner_completed_list.AgentOwnerCompletedListResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch

class OwnerCompletedViewModel : ViewModel() {

    private val agentOwnerCompletedPropertiesResponseLiveData =
        MutableLiveData<Resource<AgentOwnerCompletedListResponse>>()

    fun agentOwnerCompletedPropertiesListDetails(page: String, completed: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentOwnerCompletedPropertiesResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentOwnerCompletedPropertiesList(page, completed).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentOwnerCompletedPropertiesResponseLiveData.postValue(
                            Resource.success(
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentOwnerCompletedPropertiesResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentOwnerCompletedPropertiesResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentOwnerCompletedPropertiesResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentOwnerCompletedPropertiesResponseLiveData.postValue(
                    Resource.noInternet(
                        "",
                        null
                    )
                )
            }
        }
    }

    fun getAgentOwnerCompletedPropertiesListDetailsResponse(): LiveData<Resource<AgentOwnerCompletedListResponse>> {
        return agentOwnerCompletedPropertiesResponseLiveData
    }
}