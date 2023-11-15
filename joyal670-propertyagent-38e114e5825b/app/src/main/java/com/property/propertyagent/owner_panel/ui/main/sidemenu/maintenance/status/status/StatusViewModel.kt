package com.property.propertyagent.owner_panel.ui.main.sidemenu.maintenance.status.status

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.owner.owner_list_requested_services.OwnerListRequestedServicesResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch

class StatusViewModel: ViewModel() {
    private val ownerRequestedServiceListResponseLiveData=
        MutableLiveData<Resource<OwnerListRequestedServicesResponse>>()

    fun ownerRequestedServiceList(page:String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            ownerRequestedServiceListResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.ownerRequestedServiceList(page).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        ownerRequestedServiceListResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response.status== Constants.INTERNAL_SERVER_ERROR){
                        ownerRequestedServiceListResponseLiveData.postValue(Resource.error("null",response))
                    }
                    if(response.status== Constants.REQUEST_BAD_REQUEST){
                        ownerRequestedServiceListResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response.status== Constants.REQUEST_UNAUTHORIZED){
                        ownerRequestedServiceListResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                ownerRequestedServiceListResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getOwnerRequestedServiceListResponse(): LiveData<Resource<OwnerListRequestedServicesResponse>> {
        return ownerRequestedServiceListResponseLiveData
    }
}