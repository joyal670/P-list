package com.property.propertyagent.agent_panel.ui.main.home.myclient.users.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.CommonResponse
import com.property.propertyagent.modal.agent.agent_common_contract.AgentCommonContractResponse
import com.property.propertyagent.modal.agent.agent_user_booking_property_view_details.AgentUserBookingPropertyViewDetailsResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch

class UserPropertyViewDetailedViewModel : ViewModel() {
    private val agentUserPropertyViewDetailsResponseLiveData =
        MutableLiveData<Resource<AgentUserBookingPropertyViewDetailsResponse>>()

    private val agentOwnerContractResponseLiveData =
        MutableLiveData<Resource<AgentCommonContractResponse>>()

    private val agentUserContractResponseLiveData =
        MutableLiveData<Resource<AgentCommonContractResponse>>()

    private val agentRequestContractResponseLiveData = MutableLiveData<Resource<CommonResponse>>()

    fun agentRequestContract(user_property_id: String, tour_id: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentRequestContractResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentRequestContract(user_property_id, tour_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentRequestContractResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentRequestContractResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentRequestContractResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentRequestContractResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentRequestContractResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getAgentRequestContractDetailsResponse(): LiveData<Resource<CommonResponse>> {
        return agentRequestContractResponseLiveData
    }

    fun agentUserPropertyViewDetails(tour_id: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentUserPropertyViewDetailsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentUserPropertyViewDetails(tour_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentUserPropertyViewDetailsResponseLiveData.postValue(
                            Resource.success(
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentUserPropertyViewDetailsResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentUserPropertyViewDetailsResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentUserPropertyViewDetailsResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentUserPropertyViewDetailsResponseLiveData.postValue(
                    Resource.noInternet(
                        "",
                        null
                    )
                )
            }
        }
    }

    fun getAgentUserPropertyViewDetailsResponse(): LiveData<Resource<AgentUserBookingPropertyViewDetailsResponse>> {
        return agentUserPropertyViewDetailsResponseLiveData
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

    fun fetchAgentUserContract(property_id: String, user_id: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentUserContractResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentUserContract(property_id, user_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentUserContractResponseLiveData.postValue(
                            Resource.success(
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentUserContractResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentUserContractResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentUserContractResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentUserContractResponseLiveData.postValue(
                    Resource.noInternet(
                        "",
                        null
                    )
                )
            }
        }
    }

    fun getAgentUserContractResponse(): LiveData<Resource<AgentCommonContractResponse>> {
        return agentUserContractResponseLiveData
    }
}