package com.property.propertyuser.ui.main.maintenance.status_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.CommonResponse
import com.property.propertyuser.modal.list_requested_service_details.RequestedServiceDetailsResponse
import com.property.propertyuser.modal.status_requested_services_details.SingleRequestedServiceDetailsResponse
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Resource
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class StatusDetailsViewModel:ViewModel() {
    //RequestedServicesDetails
    private val singleRequestedServicesDetailsResponseLiveData=
        MutableLiveData<Resource<SingleRequestedServiceDetailsResponse>>()
    private val cancelRequestResponseLiveData=
        MutableLiveData<Resource<CommonResponse>>()

    fun cancelRequestedServiceDetails(request_id:String){
        val params = mapOf(
            "request_id" to request_id.toRequestBody("multipart/form-data".toMediaTypeOrNull()))
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            cancelRequestResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.cancelServiceRequest(params).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        cancelRequestResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.INTERNAL_SERVER_ERROR){
                        cancelRequestResponseLiveData.postValue(Resource.error("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        cancelRequestResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        cancelRequestResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                cancelRequestResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getCancelRequestedServiceServiceDetailsResponse(): LiveData<Resource<CommonResponse>> {
        return cancelRequestResponseLiveData
    }

    fun fetchSingleRequestedServiceDetails(request_id:String){
        val params = mapOf(
            "request_id" to request_id.toRequestBody("multipart/form-data".toMediaTypeOrNull()))
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            singleRequestedServicesDetailsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.requestedServiceDetails(params).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        singleRequestedServicesDetailsResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.INTERNAL_SERVER_ERROR){
                        singleRequestedServicesDetailsResponseLiveData.postValue(Resource.error("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        singleRequestedServicesDetailsResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        singleRequestedServicesDetailsResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                singleRequestedServicesDetailsResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getSingleRequestedServiceDetailsResponse(): LiveData<Resource<SingleRequestedServiceDetailsResponse>> {
        return singleRequestedServicesDetailsResponseLiveData
    }
}