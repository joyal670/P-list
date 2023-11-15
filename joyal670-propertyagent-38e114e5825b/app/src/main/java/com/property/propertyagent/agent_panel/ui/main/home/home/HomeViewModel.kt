package com.property.propertyagent.agent_panel.ui.main.home.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.CommonResponse
import com.property.propertyagent.modal.agent.agent_home.AgentHomeResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val logoutAgentResponseLiveData =
        MutableLiveData<Resource<CommonResponse>>()
    private val agentHomeDetailsResponseLiveData =
        MutableLiveData<Resource<AgentHomeResponse>>()

    fun agentHomeDetails() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentHomeDetailsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentHomeDetails().let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentHomeDetailsResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentHomeDetailsResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentHomeDetailsResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentHomeDetailsResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentHomeDetailsResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getAgentHomeDetailsResponse(): LiveData<Resource<AgentHomeResponse>> {
        return agentHomeDetailsResponseLiveData
    }

    fun callAgentLogout() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            logoutAgentResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.logoutAgent().let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        logoutAgentResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        logoutAgentResponseLiveData.postValue(Resource.error("null", response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        logoutAgentResponseLiveData.postValue(Resource.dataEmpty("null", response))
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        logoutAgentResponseLiveData.postValue(Resource.dataEmpty("null", response))
                    }
                }
            } catch (ex: Exception) {
                logoutAgentResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun agentLogoutResponse(): LiveData<Resource<CommonResponse>> {
        return logoutAgentResponseLiveData
    }
}