package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.myproperty

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.agent.agent_assigned_property_list.AgentAssignedPropertyListResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch

class MyPropertyViewModel : ViewModel() {

    private val agentAssignedPropertyListResponseLiveData =
        MutableLiveData<Resource<AgentAssignedPropertyListResponse>>()

    fun fetchAgentAssignedPropertyList(page: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentAssignedPropertyListResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentAssignedPropertyList(page).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentAssignedPropertyListResponseLiveData.postValue(
                            Resource.success(
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentAssignedPropertyListResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentAssignedPropertyListResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentAssignedPropertyListResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentAssignedPropertyListResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getAgentAssignedPropertyListResponse(): LiveData<Resource<AgentAssignedPropertyListResponse>> {
        return agentAssignedPropertyListResponseLiveData
    }
}