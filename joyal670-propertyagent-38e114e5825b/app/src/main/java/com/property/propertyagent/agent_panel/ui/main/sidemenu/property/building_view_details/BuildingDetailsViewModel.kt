package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.building_view_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.agent.agent_assigned_property_details.AgentAssignedPropertyDetailsResponse
import com.property.propertyagent.modal.agent.agent_builder_details.AgentBuilderDetailsResponse
import com.property.propertyagent.modal.agent.agent_property_pdf.AgentPropertyPdfResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch

class BuildingDetailsViewModel : ViewModel() {

    private val agentBuilderDetailsResponseLiveData =
        MutableLiveData<Resource<AgentBuilderDetailsResponse>>()

    private val agentApartmentDetailsResponseLiveData =
        MutableLiveData<Resource<AgentAssignedPropertyDetailsResponse>>()

    private val agentPropertyPdfResponseLiveData =
        MutableLiveData<Resource<AgentPropertyPdfResponse>>()

    fun fetchAgentBuilderDetails(property_id: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentBuilderDetailsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentBuilderDetails(property_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentBuilderDetailsResponseLiveData.postValue(
                            Resource.success(
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentBuilderDetailsResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentBuilderDetailsResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentBuilderDetailsResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentBuilderDetailsResponseLiveData.postValue(
                    Resource.noInternet(
                        "",
                        null
                    )
                )
            }
        }
    }

    fun getAgentBuilderDetailsResponse(): LiveData<Resource<AgentBuilderDetailsResponse>> {
        return agentBuilderDetailsResponseLiveData
    }

    fun fetchAgentApartmentDetails(property_id: String, building_id: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentApartmentDetailsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentApartmentDetails(property_id, building_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentApartmentDetailsResponseLiveData.postValue(
                            Resource.success(
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentApartmentDetailsResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentApartmentDetailsResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentApartmentDetailsResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentApartmentDetailsResponseLiveData.postValue(
                    Resource.noInternet(
                        "",
                        null
                    )
                )
            }
        }
    }

    fun getAgentApartmentDetailsResponse(): LiveData<Resource<AgentAssignedPropertyDetailsResponse>> {
        return agentApartmentDetailsResponseLiveData
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
                        agentPropertyPdfResponseLiveData.postValue(Resource.error("null", response))
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