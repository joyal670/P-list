package com.property.propertyuser.ui.main.maintenance.status

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.list_requested_service_details.RequestedServiceDetailsResponse
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Resource
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class StatusViewModel:ViewModel() {
    private val statusRequestedServiceResponseLiveData= MutableLiveData<Resource<RequestedServiceDetailsResponse>>()

    fun fetchRequestedServiceListDetails(user_property_id:String,page:String){
        val params = mapOf(
            "user_property_id" to user_property_id.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "page" to page.toRequestBody("multipart/form-data".toMediaTypeOrNull()))
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            statusRequestedServiceResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.listRequestService(params).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        statusRequestedServiceResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.INTERNAL_SERVER_ERROR){
                        statusRequestedServiceResponseLiveData.postValue(Resource.error("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        statusRequestedServiceResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        statusRequestedServiceResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                statusRequestedServiceResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getRequestedServiceListResponse(): LiveData<Resource<RequestedServiceDetailsResponse>> {
        return statusRequestedServiceResponseLiveData
    }
}