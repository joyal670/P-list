package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.viewpackages

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.agent.agent_package_details.AgentPackageDetailsResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch

class PackageViewModel : ViewModel() {

    private val agentPackagesResponseLiveData =
        MutableLiveData<Resource<AgentPackageDetailsResponse>>()

    fun agentPackages(property_id: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentPackagesResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentPackages(property_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentPackagesResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentPackagesResponseLiveData.postValue(Resource.error("null", response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentPackagesResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentPackagesResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentPackagesResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getAgentPackagesResponse(): LiveData<Resource<AgentPackageDetailsResponse>> {
        return agentPackagesResponseLiveData
    }
}