package com.property.propertyagent.agent_panel.ui.main.home.mytask.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.CommonResponse
import com.property.propertyagent.modal.agent.agent_pending_task_list.AgentPendingTaskListResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class MyTaskViewModel : ViewModel() {
    private val agentPendingTaskListResponseLiveData =
        MutableLiveData<Resource<AgentPendingTaskListResponse>>()

    fun fetchAgentPendingTaskList() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentPendingTaskListResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentPendingTaskList().let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentPendingTaskListResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentPendingTaskListResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentPendingTaskListResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentPendingTaskListResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentPendingTaskListResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getAgentPendingTaskListResponse(): LiveData<Resource<AgentPendingTaskListResponse>> {
        return agentPendingTaskListResponseLiveData
    }

    private val agentCompletedTaskListResponseLiveData =
        MutableLiveData<Resource<AgentPendingTaskListResponse>>()

    fun fetchAgentCompletedTaskList() {
        val params = mapOf(
            "completed" to "1".toRequestBody("multipart/form-data".toMediaTypeOrNull())
        )
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentCompletedTaskListResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentCompetedTaskList(params).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentCompletedTaskListResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentCompletedTaskListResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentCompletedTaskListResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentCompletedTaskListResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentCompletedTaskListResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getAgentCompletedTaskListResponse(): LiveData<Resource<AgentPendingTaskListResponse>> {
        return agentCompletedTaskListResponseLiveData
    }

    private val agentUpdateTaskStatusResponseLiveData =
        MutableLiveData<Resource<CommonResponse>>()

    fun fetchAgentUpdateTaskStatus(task_id: String) {
        val params = mapOf(
            "task_id" to task_id
                .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "completed" to "1".toRequestBody("multipart/form-data".toMediaTypeOrNull())
        )
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentUpdateTaskStatusResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentUpdateTaskStatus(params).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentUpdateTaskStatusResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentUpdateTaskStatusResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentUpdateTaskStatusResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentUpdateTaskStatusResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentUpdateTaskStatusResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getAgentUpdateTaskStatusResponse(): LiveData<Resource<CommonResponse>> {
        return agentUpdateTaskStatusResponseLiveData
    }

    private val agentUpdateCompletedTaskStatusResponseLiveData =
        MutableLiveData<Resource<CommonResponse>>()

    fun fetchAgentUpdateCompletedTaskStatus(task_id: String) {
        val params = mapOf(
            "task_id" to task_id
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
        )
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentUpdateCompletedTaskStatusResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentUpdateTaskStatus(params).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentUpdateCompletedTaskStatusResponseLiveData.postValue(
                            Resource.success(
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentUpdateCompletedTaskStatusResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentUpdateCompletedTaskStatusResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentUpdateCompletedTaskStatusResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentUpdateCompletedTaskStatusResponseLiveData.postValue(
                    Resource.noInternet(
                        "",
                        null
                    )
                )
            }
        }
    }

    fun getAgentUpdateCompletedTaskStatusResponse(): LiveData<Resource<CommonResponse>> {
        return agentUpdateCompletedTaskStatusResponseLiveData
    }
}