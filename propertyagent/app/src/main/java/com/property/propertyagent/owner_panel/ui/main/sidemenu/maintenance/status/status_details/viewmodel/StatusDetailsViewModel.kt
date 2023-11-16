package com.property.propertyagent.owner_panel.ui.main.sidemenu.maintenance.status.status_details.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.CommonResponse
import com.property.propertyagent.modal.owner.owner_request_service_details.OwnerRequestedServiceDetailsResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class StatusDetailsViewModel : ViewModel() {
    private val ownerRequestedServiceDetailsResponseLiveData =
        MutableLiveData<Resource<OwnerRequestedServiceDetailsResponse>>()

    fun ownerRequestedServiceDetails(request_id : String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            ownerRequestedServiceDetailsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.ownerRequestedServiceDetails(request_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        ownerRequestedServiceDetailsResponseLiveData.postValue(Resource.success(
                            response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        ownerRequestedServiceDetailsResponseLiveData.postValue(Resource.error("null" ,
                            response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        ownerRequestedServiceDetailsResponseLiveData.postValue(Resource.dataEmpty("null" ,
                            response))
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        ownerRequestedServiceDetailsResponseLiveData.postValue(Resource.dataEmpty("null" ,
                            response))
                    }
                }

            } catch (ex : Exception) {
                ownerRequestedServiceDetailsResponseLiveData.postValue(Resource.noInternet("" ,
                    null))
            }
        }
    }

    fun getOwnerRequestedServiceDetailsResponse() : LiveData<Resource<OwnerRequestedServiceDetailsResponse>> {
        return ownerRequestedServiceDetailsResponseLiveData
    }

    private val cancelOwnerServiceLiveData = MutableLiveData<Resource<CommonResponse>>()

    fun cancelOwnerServiceDetails(request_id : String) {
        val params = mapOf(
            "request_id" to request_id.toRequestBody("multipart/form-data".toMediaTypeOrNull()))
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            cancelOwnerServiceLiveData.postValue(Resource.loading(null))
            try {
                repository.cancelServiceRequest(params).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        cancelOwnerServiceLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        cancelOwnerServiceLiveData.postValue(Resource.error("null" , response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        cancelOwnerServiceLiveData.postValue(Resource.dataEmpty(response.response , response))
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        cancelOwnerServiceLiveData.postValue(Resource.dataEmpty("null" , response))
                    }
                }

            } catch (ex : Exception) {
                cancelOwnerServiceLiveData.postValue(Resource.noInternet("" , null))
            }
        }
    }

    fun getCancelOwnerServiceResponse() : LiveData<Resource<CommonResponse>> {
        return cancelOwnerServiceLiveData
    }
}