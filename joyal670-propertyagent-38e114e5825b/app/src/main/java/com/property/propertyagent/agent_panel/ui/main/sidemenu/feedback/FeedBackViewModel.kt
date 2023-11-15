package com.property.propertyagent.agent_panel.ui.main.sidemenu.feedback

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.agent.agent_feedback.AgentFeedBackResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch

class FeedBackViewModel : ViewModel() {

    private val agentFeedBackResponseLiveData =
        MutableLiveData<Resource<AgentFeedBackResponse>>()

    fun agentFeedBack(page: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentFeedBackResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentFeedBack(page).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentFeedBackResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentFeedBackResponseLiveData.postValue(Resource.error("null", response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentFeedBackResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentFeedBackResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentFeedBackResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getAgentFeedBackResponse(): LiveData<Resource<AgentFeedBackResponse>> {
        return agentFeedBackResponseLiveData
    }
}