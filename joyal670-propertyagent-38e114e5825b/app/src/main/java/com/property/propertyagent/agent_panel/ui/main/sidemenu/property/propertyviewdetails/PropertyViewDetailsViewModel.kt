package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertyviewdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.agent.agent_assigned_property_details.AgentAssignedPropertyDetailsResponse
import com.property.propertyagent.modal.agent.agent_property_pdf.AgentPropertyPdfResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch

class PropertyViewDetailsViewModel : ViewModel() {
    private val propertyDetailsResponseLiveData =
        MutableLiveData<Resource<AgentAssignedPropertyDetailsResponse>>()

    private val agentPropertyPdfResponseLiveData =
        MutableLiveData<Resource<AgentPropertyPdfResponse>>()

    fun agentPropertyViewDetails(property_id: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            propertyDetailsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentAssignedPropertyDetails(property_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        propertyDetailsResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        propertyDetailsResponseLiveData.postValue(Resource.error("null", response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        propertyDetailsResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        propertyDetailsResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                propertyDetailsResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getAgentPropertyViewDetailsResponse(): LiveData<Resource<AgentAssignedPropertyDetailsResponse>> {
        return propertyDetailsResponseLiveData
    }

    fun agentPropertyPdf(property_id: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentPropertyPdfResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentPropertyPdf(property_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentPropertyPdfResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentPropertyPdfResponseLiveData.postValue(Resource.error(response.response, response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentPropertyPdfResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentPropertyPdfResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentPropertyPdfResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getAgentPropertyPdfResponse(): LiveData<Resource<AgentPropertyPdfResponse>> {
        return agentPropertyPdfResponseLiveData
    }
}