package com.property.propertyuser.ui.main.maintenance.service

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.service_list.ServiceListResponse
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Resource
import kotlinx.coroutines.launch

class ServiceViewModel:ViewModel() {
    private val serviceListResponseLiveData= MutableLiveData<Resource<ServiceListResponse>>()

    fun fetchServiceListDetails(service_name: String,page:String,property_id:String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            serviceListResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.serviceListDetails(service_name,page,property_id).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        serviceListResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.INTERNAL_SERVER_ERROR){
                        serviceListResponseLiveData.postValue(Resource.error("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        serviceListResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        serviceListResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                serviceListResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getServiceListResponse(): LiveData<Resource<ServiceListResponse>> {
        return serviceListResponseLiveData
    }
}