package com.property.propertyuser.ui.main.my_property.view_details.vacate_request

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.CommonResponse
import com.property.propertyuser.modal.vacate_request_list.VacateRequestListResponse
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Resource
import kotlinx.coroutines.launch

class VacateRequestViewModel:ViewModel() {
    private val vacateRequestResponseLiveData=
        MutableLiveData<Resource<CommonResponse>>()
    private val vacateRequestListResponseLiveData=
        MutableLiveData<Resource<VacateRequestListResponse>>()

    fun vacateRequestList(page:String, property_id: String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            vacateRequestListResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.vacateRequestList(page, property_id).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        vacateRequestListResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.INTERNAL_SERVER_ERROR){
                        vacateRequestListResponseLiveData.postValue(Resource.error("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        vacateRequestListResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        vacateRequestListResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                vacateRequestListResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getVacateRequestListResponse(): LiveData<Resource<VacateRequestListResponse>> {
        return vacateRequestListResponseLiveData
    }
    fun vacateRequest(user_property_id:String,vacate_date:String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            vacateRequestResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.vacateRequestForProperty(user_property_id,vacate_date).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        vacateRequestResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.INTERNAL_SERVER_ERROR){
                        vacateRequestResponseLiveData.postValue(Resource.error("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        vacateRequestResponseLiveData.postValue(Resource.dataEmpty(response.response,response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        vacateRequestResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                vacateRequestResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getVacateRequestResponse(): LiveData<Resource<CommonResponse>> {
        return vacateRequestResponseLiveData
    }
}