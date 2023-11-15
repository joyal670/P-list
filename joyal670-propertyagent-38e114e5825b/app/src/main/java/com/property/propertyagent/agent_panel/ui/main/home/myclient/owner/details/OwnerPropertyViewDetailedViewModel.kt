package com.property.propertyagent.agent_panel.ui.main.home.myclient.owner.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.agent.agent_common_contract.AgentCommonContractResponse
import com.property.propertyagent.modal.agent.agent_start_owner_tour_property_details.AgentStartOwnerTourPropertyDetailsResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch

class OwnerPropertyViewDetailedViewModel : ViewModel() {

    private val agentStartOwnerTourPropertyDetailsResponseLiveData =
        MutableLiveData<Resource<AgentStartOwnerTourPropertyDetailsResponse>>()

    private val agentOwnerContractResponseLiveData =
        MutableLiveData<Resource<AgentCommonContractResponse>>()

    fun fetchAgentStartOwnerTourPropertyDetails(tour_id: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentStartOwnerTourPropertyDetailsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentStartOwnerTourPropertyDetails(tour_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentStartOwnerTourPropertyDetailsResponseLiveData.postValue(
                            Resource.success(
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentStartOwnerTourPropertyDetailsResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentStartOwnerTourPropertyDetailsResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentStartOwnerTourPropertyDetailsResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentStartOwnerTourPropertyDetailsResponseLiveData.postValue(
                    Resource.noInternet(
                        "",
                        null
                    )
                )
            }
        }
    }

    fun fetchAgentStartOwnerTourPropertyDetails2(tour_id: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentStartOwnerTourPropertyDetailsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentMyOwnerPropertyDetails(tour_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentStartOwnerTourPropertyDetailsResponseLiveData.postValue(
                            Resource.success(
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentStartOwnerTourPropertyDetailsResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentStartOwnerTourPropertyDetailsResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentStartOwnerTourPropertyDetailsResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentStartOwnerTourPropertyDetailsResponseLiveData.postValue(
                    Resource.noInternet(
                        "",
                        null
                    )
                )
            }
        }
    }

    fun getAgentStartOwnerTourPropertyDetailsResponse(): LiveData<Resource<AgentStartOwnerTourPropertyDetailsResponse>> {
        return agentStartOwnerTourPropertyDetailsResponseLiveData
    }

    fun fetchAgentOwnerContract(property_id: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentOwnerContractResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentOwnerContract(property_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentOwnerContractResponseLiveData.postValue(
                            Resource.success(
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentOwnerContractResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentOwnerContractResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentOwnerContractResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentOwnerContractResponseLiveData.postValue(
                    Resource.noInternet(
                        "",
                        null
                    )
                )
            }
        }
    }

    fun getAgentOwnerContractResponse(): LiveData<Resource<AgentCommonContractResponse>> {
        return agentOwnerContractResponseLiveData
    }
}