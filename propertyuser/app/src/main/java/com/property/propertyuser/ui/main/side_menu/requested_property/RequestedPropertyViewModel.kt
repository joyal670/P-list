package com.property.propertyuser.ui.main.side_menu.requested_property

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.desired_property_request_list.DesiredPropertyRequestListResponse
import com.property.propertyuser.modal.fetch_requested_property_data.FetchRequestedPropertyDataResponse
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Resource
import kotlinx.coroutines.launch

class RequestedPropertyViewModel: ViewModel() {
    private val desiredRequestedPropertyListResponseLiveData=
        MutableLiveData<Resource<DesiredPropertyRequestListResponse>>()

    fun fetchDesiredRequestedPropertyList(page:String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            desiredRequestedPropertyListResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.desiredPropertyRequestList(page).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        desiredRequestedPropertyListResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        desiredRequestedPropertyListResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        desiredRequestedPropertyListResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                desiredRequestedPropertyListResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getDesiredRequestedPropertyListResponse(): LiveData<Resource<DesiredPropertyRequestListResponse>> {
        return desiredRequestedPropertyListResponseLiveData
    }
}