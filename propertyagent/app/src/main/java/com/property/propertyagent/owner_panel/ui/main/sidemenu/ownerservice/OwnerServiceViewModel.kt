package com.property.propertyagent.owner_panel.ui.main.sidemenu.ownerservice

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.CommonResponse
import com.property.propertyagent.modal.owner.owner_request_service_for_approval_list.OwnerRequestServiceForApprovalListResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class OwnerServiceViewModel:ViewModel() {
    private val ownerRequestedServiceForApprovalLiveData= MutableLiveData<Resource<OwnerRequestServiceForApprovalListResponse>>()

    fun ownerRequestedServiceForApprovalList(page: String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            ownerRequestedServiceForApprovalLiveData.postValue(Resource.loading(null))
            try {
                repository.ownerRequestedServiceForApprovalList(page).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        ownerRequestedServiceForApprovalLiveData.postValue(Resource.success(response))
                    }
                    if(response.status== Constants.INTERNAL_SERVER_ERROR){
                        ownerRequestedServiceForApprovalLiveData.postValue(Resource.error("null",response))
                    }
                    if(response.status== Constants.REQUEST_BAD_REQUEST){
                        ownerRequestedServiceForApprovalLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response.status== Constants.REQUEST_UNAUTHORIZED){
                        ownerRequestedServiceForApprovalLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                Log.e("TAG", "ownerRequestedServiceForApprovalList: "+ ex )
                ownerRequestedServiceForApprovalLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getOwnerRequestedServiceForApprovalListResponse(): LiveData<Resource<OwnerRequestServiceForApprovalListResponse>> {
        return ownerRequestedServiceForApprovalLiveData
    }

    private val ownerAcceptRequestedServiceLiveData= MutableLiveData<Resource<CommonResponse>>()

    fun ownerAcceptRequestedServiceDetails(request_id: String){
        val params = mapOf(
            "request_id" to request_id.toRequestBody("multipart/form-data".toMediaTypeOrNull()))
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            ownerAcceptRequestedServiceLiveData.postValue(Resource.loading(null))
            try {
                repository.ownerAcceptRequestedService(params).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        ownerAcceptRequestedServiceLiveData.postValue(Resource.success(response))
                    }
                    if(response.status== Constants.INTERNAL_SERVER_ERROR){
                        ownerAcceptRequestedServiceLiveData.postValue(Resource.error("null",response))
                    }
                    if(response.status== Constants.REQUEST_BAD_REQUEST){
                        ownerAcceptRequestedServiceLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response.status== Constants.REQUEST_UNAUTHORIZED){
                        ownerAcceptRequestedServiceLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                ownerAcceptRequestedServiceLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getOwnerAcceptRequestedServiceResponse(): LiveData<Resource<CommonResponse>> {
        return ownerAcceptRequestedServiceLiveData
    }

    private val ownerRejectRequestedServiceLiveData= MutableLiveData<Resource<CommonResponse>>()

    fun ownerRejectRequestedService(request_id: String){
        val params = mapOf(
            "request_id" to request_id.toRequestBody("multipart/form-data".toMediaTypeOrNull()))
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            ownerRejectRequestedServiceLiveData.postValue(Resource.loading(null))
            try {
                repository.ownerRejectRequestedService(params).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        ownerRejectRequestedServiceLiveData.postValue(Resource.success(response))
                    }
                    if(response.status== Constants.INTERNAL_SERVER_ERROR){
                        ownerRejectRequestedServiceLiveData.postValue(Resource.error("null",response))
                    }
                    if(response.status== Constants.REQUEST_BAD_REQUEST){
                        ownerRejectRequestedServiceLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response.status== Constants.REQUEST_UNAUTHORIZED){
                        ownerRejectRequestedServiceLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                ownerRejectRequestedServiceLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getOwnerRejectRequestedServiceResponse(): LiveData<Resource<CommonResponse>> {
        return ownerRejectRequestedServiceLiveData
    }
}