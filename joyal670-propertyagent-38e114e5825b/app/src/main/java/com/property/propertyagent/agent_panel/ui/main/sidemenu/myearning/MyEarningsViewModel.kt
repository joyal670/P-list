package com.property.propertyagent.agent_panel.ui.main.sidemenu.myearning

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.CommonResponse
import com.property.propertyagent.modal.agent.my_earnings.MyEarningsListResponse
import com.property.propertyagent.modal.agent.my_earnings.TotalEarningsResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch

class MyEarningsViewModel : ViewModel() {

    private val agentMyEarningsTotalResponseLiveData =
        MutableLiveData<Resource<TotalEarningsResponse>>()

    private val agentMyEarningsListResponseLiveData =
        MutableLiveData<Resource<MyEarningsListResponse>>()

    private val agentMyEarningsRequestResponseLiveData =
        MutableLiveData<Resource<CommonResponse>>()

    fun myEarningsTotal() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentMyEarningsTotalResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentTotalMyEarnings().let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentMyEarningsTotalResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentMyEarningsTotalResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentMyEarningsTotalResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentMyEarningsTotalResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentMyEarningsTotalResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getAgentMyEarningsTotalResponse(): LiveData<Resource<TotalEarningsResponse>> {
        return agentMyEarningsTotalResponseLiveData
    }

    fun myEarningsList(page: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentMyEarningsListResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentMyEarningsList(page).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentMyEarningsListResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentMyEarningsListResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentMyEarningsListResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentMyEarningsListResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentMyEarningsTotalResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getAgentMyEarningsListResponse(): LiveData<Resource<MyEarningsListResponse>> {
        return agentMyEarningsListResponseLiveData
    }

    fun myEarningsRequest() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentMyEarningsRequestResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentRequestMyEarnings().let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentMyEarningsRequestResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentMyEarningsRequestResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentMyEarningsRequestResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentMyEarningsRequestResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentMyEarningsRequestResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getAgentMyEarningsRequestResponse(): LiveData<Resource<CommonResponse>> {
        return agentMyEarningsRequestResponseLiveData
    }
}