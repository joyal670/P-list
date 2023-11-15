package com.property.propertyagent.agent_panel.ui.main.home.request

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.CommonResponse
import com.property.propertyagent.modal.agent.agent_my_request_list.AgentMyRequestListResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class RequestViewModel : ViewModel() {

    private val agentRequestListResponseLiveData =
        MutableLiveData<Resource<AgentMyRequestListResponse>>()

    fun fetchAgentRequestList() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentRequestListResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentRequestList().let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentRequestListResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentRequestListResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentRequestListResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentRequestListResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentRequestListResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getAgentRequestListResponse(): LiveData<Resource<AgentMyRequestListResponse>> {
        return agentRequestListResponseLiveData
    }

    private val agentAcceptRequestResponseLiveData =
        MutableLiveData<Resource<CommonResponse>>()

    fun fetchAgentAcceptRequest(request_id: String, type: String) {
        val params = mapOf(
            "request_id" to request_id
                .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "type" to type.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        )
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentAcceptRequestResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentAcceptMyRequest(params).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentAcceptRequestResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentAcceptRequestResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentAcceptRequestResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentAcceptRequestResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentAcceptRequestResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getAgentAcceptRequestResponse(): LiveData<Resource<CommonResponse>> {
        return agentAcceptRequestResponseLiveData
    }

    private val agentRejectRequestResponseLiveData =
        MutableLiveData<Resource<CommonResponse>>()

    fun fetchAgentRejectRequest(request_id: String, type: String) {
        val params = mapOf(
            "request_id" to request_id
                .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "type" to type.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        )
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentAcceptRequestResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentRejectMyRequest(params).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentRejectRequestResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentRejectRequestResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentRejectRequestResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentRejectRequestResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }

            } catch (ex: Exception) {
                agentRejectRequestResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getAgentRejectRequestResponse(): LiveData<Resource<CommonResponse>> {
        return agentRejectRequestResponseLiveData
    }
}