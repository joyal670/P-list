package com.iroid.patrickstore.ui.shop_details.services.service_details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.iroid.patrickstore.api.ApiRepositoryProvider
import com.iroid.patrickstore.model.service.service_list.ServicelistResponse
import com.iroid.patrickstore.model.service.service_request.ServiceRequestResponse
import com.iroid.patrickstore.utils.Constants
import com.iroid.patrickstore.utils.Resource
import kotlinx.coroutines.launch

class ServiceDetailViewModal : ViewModel() {
    private val liveDataServiceDetail = MutableLiveData<Resource<ServicelistResponse>>()
    val serviceDetailLiveData: LiveData<Resource<ServicelistResponse>> get() = liveDataServiceDetail

    private val liveDataServiceQuote = MutableLiveData<Resource<ServiceRequestResponse>>()
    val serviceQuoteLiveData: LiveData<Resource<ServiceRequestResponse>> get() = liveDataServiceQuote



    fun addServiceQuote(serviceId:String,
                        sellerId:String,
                        rate:String,
                        date:String,
                        time:String,
                        quantity:String,
                        customerMsg:String){
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                liveDataServiceQuote.postValue(Resource.loading(null))
                repository.newQuote(serviceId,sellerId,rate,date,time, quantity,customerMsg).let {
                    val response=it.body()
                    when(response!!.statusCode){
                        Constants.REQUEST_OK -> {
                            liveDataServiceQuote.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveDataServiceQuote.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveDataServiceQuote.postValue(Resource.error(response.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveDataServiceQuote.postValue(Resource.noInterNet("", null))
                        }
                    }
                }
            }catch (ex: Exception) {
                liveDataServiceQuote.postValue(Resource.noInterNet("", null))
            }
        }
    }

    fun getServiceDetail(serviceId:String){
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                liveDataServiceDetail.postValue(Resource.loading(null))
                repository.getSellerService(1,10,serviceId).let {
                    val response=it.body()
                    when(response!!.statusCode){
                        Constants.REQUEST_OK -> {
                            liveDataServiceDetail.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveDataServiceDetail.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveDataServiceDetail.postValue(Resource.error(response.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveDataServiceDetail.postValue(Resource.noInterNet("", null))
                        }
                    }
                }
            }catch (ex: Exception) {
                liveDataServiceDetail.postValue(Resource.noInterNet("", null))
            }

        }
    }
}
