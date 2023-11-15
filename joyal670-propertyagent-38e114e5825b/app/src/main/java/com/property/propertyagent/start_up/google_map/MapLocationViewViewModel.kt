package com.property.propertyagent.start_up.google_map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.agent.home_location_tour.AgentTourLocationViewResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch

class MapLocationViewViewModel : ViewModel() {

    private val agentLocationViewResponseLiveData =
        MutableLiveData<Resource<AgentTourLocationViewResponse>>()

    fun tourLocationViewData(tour_id: String, user_type: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentLocationViewResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentHomeTourLocationViewData(tour_id, user_type).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentLocationViewResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentLocationViewResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentLocationViewResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentLocationViewResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }

            } catch (ex: Exception) {
                agentLocationViewResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getAgentTourLocationViewDataResponse(): LiveData<Resource<AgentTourLocationViewResponse>> {
        return agentLocationViewResponseLiveData
    }
}