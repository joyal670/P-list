package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.terms_and_stay

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.agent.agent_terms_of_stay.TermsOfStayResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch

class TermsOfStayViewModel : ViewModel() {
    private val agentTermsResponseLiveData =
        MutableLiveData<Resource<TermsOfStayResponse>>()

    fun agentTermsOfStay(property_id: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentTermsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentTermsOfStay(property_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentTermsResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentTermsResponseLiveData.postValue(Resource.error("null", response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentTermsResponseLiveData.postValue(Resource.dataEmpty("null", response))
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentTermsResponseLiveData.postValue(Resource.dataEmpty("null", response))
                    }
                }
            } catch (ex: Exception) {
                agentTermsResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getAgentTermsOfStayResponse(): LiveData<Resource<TermsOfStayResponse>> {
        return agentTermsResponseLiveData
    }

}