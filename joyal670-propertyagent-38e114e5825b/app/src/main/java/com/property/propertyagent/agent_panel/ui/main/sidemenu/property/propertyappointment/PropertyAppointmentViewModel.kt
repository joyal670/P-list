package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertyappointment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.CommonResponse
import com.property.propertyagent.modal.agent.agent_property_appointment_start_tour.AgentPropertyAppointmentResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch

class PropertyAppointmentViewModel : ViewModel() {
    private val agentPropertyAppointmentDetailsResponseLiveData =
        MutableLiveData<Resource<AgentPropertyAppointmentResponse>>()

    private val agentUpdateTourNotInterestedResponseLiveData =
        MutableLiveData<Resource<CommonResponse>>()

    private val agentAddCommissionResponseLiveData =
        MutableLiveData<Resource<CommonResponse>>()

    private val agentEditCommissionResponseLiveData =
        MutableLiveData<Resource<CommonResponse>>()

    fun agentUpdateTourNotInterested(tour_id: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentUpdateTourNotInterestedResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentUpdateTourNotInterested(tour_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentUpdateTourNotInterestedResponseLiveData.postValue(
                            Resource.success(
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentUpdateTourNotInterestedResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentUpdateTourNotInterestedResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentUpdateTourNotInterestedResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentUpdateTourNotInterestedResponseLiveData.postValue(
                    Resource.noInternet(
                        "",
                        null
                    )
                )
            }
        }
    }

    fun getAgentUpdateTourNotInterestedResponse(): LiveData<Resource<CommonResponse>> {
        return agentUpdateTourNotInterestedResponseLiveData
    }

    fun agentPropertyAppointmentDetails(tour_id: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentPropertyAppointmentDetailsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentPropertyAppointment(tour_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentPropertyAppointmentDetailsResponseLiveData.postValue(
                            Resource.success(
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentPropertyAppointmentDetailsResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentPropertyAppointmentDetailsResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentPropertyAppointmentDetailsResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentPropertyAppointmentDetailsResponseLiveData.postValue(
                    Resource.noInternet(
                        "",
                        null
                    )
                )
            }
        }
    }

    fun getAgentPropertyAppointmentDetailsResponse(): LiveData<Resource<AgentPropertyAppointmentResponse>> {
        return agentPropertyAppointmentDetailsResponseLiveData
    }

    fun agentAddCommission(tour_id: Int, user_id: Int, property_id: Int, amount: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentAddCommissionResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentAddCommission(tour_id, user_id, property_id, amount).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentAddCommissionResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentAddCommissionResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentAddCommissionResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentAddCommissionResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentAddCommissionResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getAgentAddCommissionResponse(): LiveData<Resource<CommonResponse>> {
        return agentAddCommissionResponseLiveData
    }

    fun agentEditCommission(tour_id: Int, amount: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentEditCommissionResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentEditCommission(tour_id, amount).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentEditCommissionResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentEditCommissionResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentEditCommissionResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentEditCommissionResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentEditCommissionResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getAgentEditCommissionResponse(): LiveData<Resource<CommonResponse>> {
        return agentEditCommissionResponseLiveData
    }
}