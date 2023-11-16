package com.property.propertyagent.agent_panel.ui.main.sidemenu.inhandcash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.CommonResponse
import com.property.propertyagent.modal.agent.agent_cash_in_hand.AgentCashInHandTotalResponse
import com.property.propertyagent.modal.agent.agent_cash_in_hand.CashInHandListResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch

class CashInHandViewModel : ViewModel() {

    private val agentCashInHandTotalResponseLiveData =
        MutableLiveData<Resource<AgentCashInHandTotalResponse>>()

    private val agentPayCashInHandTotalResponseLiveData =
        MutableLiveData<Resource<CommonResponse>>()

    private val agentCashInHandListResponseLiveData =
        MutableLiveData<Resource<CashInHandListResponse>>()

    fun cashInHandTotal() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentCashInHandTotalResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentTotalCashInHand().let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentCashInHandTotalResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentCashInHandTotalResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentCashInHandTotalResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentCashInHandTotalResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentCashInHandTotalResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getAgentCashInHandResponse(): LiveData<Resource<AgentCashInHandTotalResponse>> {
        return agentCashInHandTotalResponseLiveData
    }

    fun payCashInHand() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentPayCashInHandTotalResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentPayTotalCashInHand().let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentPayCashInHandTotalResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentPayCashInHandTotalResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentPayCashInHandTotalResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentPayCashInHandTotalResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentPayCashInHandTotalResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getAgentPayCashInHandResponse(): LiveData<Resource<CommonResponse>> {
        return agentPayCashInHandTotalResponseLiveData
    }

    fun cashInHandList(page: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentPayCashInHandTotalResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentCashInHandList(page).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentCashInHandListResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentCashInHandListResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentCashInHandListResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentCashInHandListResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentCashInHandListResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getAgentCashInHandListResponse(): LiveData<Resource<CashInHandListResponse>> {
        return agentCashInHandListResponseLiveData
    }
}