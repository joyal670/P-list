package com.property.propertyagent.agent_panel.ui.main.home.mytask.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.CommonResponse
import com.property.propertyagent.modal.agent.agent_calender_task_count.AgentCalenderTaskCountResponse
import com.property.propertyagent.modal.agent.agent_calender_task_list.AgentCalenderTaskListResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class MyTaskCalenderViewViewModel : ViewModel() {
    private val agentAddTaskResponseLiveData =
        MutableLiveData<Resource<CommonResponse>>()

    fun agentAddTaskDetails(title : String , task_date : String , task_time : String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentAddTaskResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentAddTask(title , task_date , task_time).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentAddTaskResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentAddTaskResponseLiveData.postValue(Resource.error("null" , response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentAddTaskResponseLiveData.postValue(Resource.dataEmpty(response.response ,
                            response))
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentAddTaskResponseLiveData.postValue(Resource.dataEmpty("null" ,
                            response))
                    }
                }
            } catch (ex : Exception) {
                agentAddTaskResponseLiveData.postValue(Resource.noInternet("" , null))
            }
        }
    }

    fun getAgentAddTaskResponse() : LiveData<Resource<CommonResponse>> {
        return agentAddTaskResponseLiveData
    }

    private val agentCalenderTaskCountResponseLiveData =
        MutableLiveData<Resource<AgentCalenderTaskCountResponse>>()

    fun agentCalenderTaskCountDetails(date : String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentCalenderTaskCountResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentCalenderTaskCount(date).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentCalenderTaskCountResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentCalenderTaskCountResponseLiveData.postValue(Resource.error("null" ,
                            response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentCalenderTaskCountResponseLiveData.postValue(Resource.dataEmpty("null" ,
                            response))
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentCalenderTaskCountResponseLiveData.postValue(Resource.dataEmpty("null" ,
                            response))
                    }
                }
            } catch (ex : Exception) {
                agentCalenderTaskCountResponseLiveData.postValue(Resource.noInternet("" , null))
            }
        }
    }

    fun getAgentCalenderTaskCountResponse() : LiveData<Resource<AgentCalenderTaskCountResponse>> {
        return agentCalenderTaskCountResponseLiveData
    }

    private val agentCalenderTaskListResponseLiveData =
        MutableLiveData<Resource<AgentCalenderTaskListResponse>>()

    fun fetchAgentCalenderTaskList(date : String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentCalenderTaskListResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentCalenderTaskList(date).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentCalenderTaskListResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentCalenderTaskListResponseLiveData.postValue(Resource.error("null" ,
                            response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentCalenderTaskListResponseLiveData.postValue(Resource.dataEmpty("null" ,
                            response))
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentCalenderTaskListResponseLiveData.postValue(Resource.dataEmpty("null" ,
                            response))
                    }
                }
            } catch (ex : Exception) {
                agentCalenderTaskListResponseLiveData.postValue(Resource.noInternet("" , null))
            }
        }
    }

    fun getAgentCalenderTaskListResponse() : LiveData<Resource<AgentCalenderTaskListResponse>> {
        return agentCalenderTaskListResponseLiveData
    }

    private val agentUpdateTaskStatusResponseLiveData =
        MutableLiveData<Resource<CommonResponse>>()

    fun fetchAgentUpdateTaskStatus(task_id : String) {
        val params = mapOf(
            "task_id" to task_id
                .toRequestBody("multipart/form-data".toMediaTypeOrNull()) ,
            "completed" to "1".toRequestBody("multipart/form-data".toMediaTypeOrNull()))
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
                        agentUpdateTaskStatusResponseLiveData.postValue(Resource.error("null" ,
                            response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentUpdateTaskStatusResponseLiveData.postValue(Resource.dataEmpty("null" ,
                            response))
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentUpdateTaskStatusResponseLiveData.postValue(Resource.dataEmpty("null" ,
                            response))
                    }
                }
            } catch (ex : Exception) {
                agentUpdateTaskStatusResponseLiveData.postValue(Resource.noInternet("" , null))
            }
        }
    }

    fun getAgentUpdateTaskStatusResponse() : LiveData<Resource<CommonResponse>> {
        return agentUpdateTaskStatusResponseLiveData
    }

    private val agentUpdateCompletedTaskStatusResponseLiveData =
        MutableLiveData<Resource<CommonResponse>>()

    fun fetchAgentUpdateCompletedTaskStatus(task_id : String) {
        val params = mapOf(
            "task_id" to task_id
                .toRequestBody("multipart/form-data".toMediaTypeOrNull()))
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentUpdateCompletedTaskStatusResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentUpdateTaskStatus(params).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentUpdateCompletedTaskStatusResponseLiveData.postValue(Resource.success(
                            response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentUpdateCompletedTaskStatusResponseLiveData.postValue(Resource.error("null" ,
                            response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentUpdateCompletedTaskStatusResponseLiveData.postValue(Resource.dataEmpty(
                            "null" ,
                            response))
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentUpdateCompletedTaskStatusResponseLiveData.postValue(Resource.dataEmpty(
                            "null" ,
                            response))
                    }
                }
            } catch (ex : Exception) {
                agentUpdateCompletedTaskStatusResponseLiveData.postValue(Resource.noInternet("" ,
                    null))
            }
        }
    }

    fun getAgentUpdateCompletedTaskStatusResponse() : LiveData<Resource<CommonResponse>> {
        return agentUpdateCompletedTaskStatusResponseLiveData
    }
}