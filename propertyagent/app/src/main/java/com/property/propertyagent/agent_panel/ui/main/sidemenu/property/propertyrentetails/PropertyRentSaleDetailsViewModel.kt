package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertyrentetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.agent.agent_property_rent_details.AgentPropertyRentDetailsResponse
import com.property.propertyagent.modal.agent.agent_property_sale_details.AgentPropertySaleDetailsResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch

class PropertyRentSaleDetailsViewModel : ViewModel() {

    private val propertyRentDetailsResponseLiveData =
        MutableLiveData<Resource<AgentPropertyRentDetailsResponse>>()

    private val propertySaleDetailsResponseLiveData =
        MutableLiveData<Resource<AgentPropertySaleDetailsResponse>>()

    fun agentPropertyRentDetails(property_id: String, type: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            propertyRentDetailsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentPropertyRentDetails(property_id, type).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        propertyRentDetailsResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        propertyRentDetailsResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        propertyRentDetailsResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        propertyRentDetailsResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                propertyRentDetailsResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getAgentPropertyRentDetailsResponse(): LiveData<Resource<AgentPropertyRentDetailsResponse>> {
        return propertyRentDetailsResponseLiveData
    }

    fun agentPropertySaleDetails(property_id: String, type: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            propertySaleDetailsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentPropertySaleDetails(property_id, type).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        propertySaleDetailsResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        propertySaleDetailsResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        propertySaleDetailsResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        propertySaleDetailsResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                propertySaleDetailsResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getAgentPropertySaleDetailsResponse(): LiveData<Resource<AgentPropertySaleDetailsResponse>> {
        return propertySaleDetailsResponseLiveData
    }
}