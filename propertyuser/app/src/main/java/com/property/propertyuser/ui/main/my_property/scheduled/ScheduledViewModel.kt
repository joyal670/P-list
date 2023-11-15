package com.property.propertyuser.ui.main.my_property.scheduled

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.my_property_list.MyPropertiesResponse
import com.property.propertyuser.modal.scheduled_property.ScheduledPropertyListResponse
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Resource
import kotlinx.coroutines.launch

class ScheduledViewModel:ViewModel() {

    private val scheduledPropertyListResponseLiveData= MutableLiveData<Resource<ScheduledPropertyListResponse>>()

    fun fetchScheduledPropertiesList(page: String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            scheduledPropertyListResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.scheduledPropertyList(page).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        scheduledPropertyListResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        scheduledPropertyListResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        scheduledPropertyListResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status==Constants.INTERNAL_SERVER_ERROR){
                        scheduledPropertyListResponseLiveData.postValue(Resource.error("null",response))
                    }
                }

            }catch (ex:Exception){
                scheduledPropertyListResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getScheduledPropertiesListResponse(): LiveData<Resource<ScheduledPropertyListResponse>> {
        return scheduledPropertyListResponseLiveData
    }
}