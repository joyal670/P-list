package com.property.propertyagent.owner_panel.ui.main.home.payment.payment_history.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.owner.owner_payment_history_list.OwnerPaymentHistoryListResponse
import com.property.propertyagent.modal.owner.owner_property_payment_details.PropertyPaymentDetailsResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch

class PropertyPaymentDetailsViewModel : ViewModel() {
    private val ownerPaymentResponseLiveData =
        MutableLiveData<Resource<PropertyPaymentDetailsResponse>>()

    private val ownerPaymentHistoryListResponseLiveData =
        MutableLiveData<Resource<OwnerPaymentHistoryListResponse>>()

    /* property details api */
    fun paymentListOfProperties(property_id : Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            ownerPaymentResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.ownerPropertyPaymentDetails(property_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        ownerPaymentResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        ownerPaymentResponseLiveData.postValue(Resource.error("null" , response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        ownerPaymentResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null" ,
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        ownerPaymentResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null" ,
                                response
                            )
                        )
                    }
                }

            } catch (ex : Exception) {
                ownerPaymentResponseLiveData.postValue(Resource.noInternet("" , null))
                Log.e("TAG" , "paymentListOfProperties: $ex")
            }
        }
    }

    /* property details */
    fun getOwnerPaymentResponse() : LiveData<Resource<PropertyPaymentDetailsResponse>> {
        return ownerPaymentResponseLiveData
    }

    /* property payment history details api */
    fun paymentHistoryListProperties(property_id : Int,page : String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            ownerPaymentHistoryListResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.ownerPropertyHistoryList(property_id, page).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        ownerPaymentHistoryListResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        ownerPaymentHistoryListResponseLiveData.postValue(Resource.error("null" , response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        ownerPaymentHistoryListResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null" ,
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        ownerPaymentHistoryListResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null" ,
                                response
                            )
                        )
                    }
                }

            } catch (ex : Exception) {
                ownerPaymentHistoryListResponseLiveData.postValue(Resource.noInternet("" , null))
                Log.e("TAG" , "paymentListOfProperties: $ex")
            }
        }
    }

    /* payment history list */
    fun getOwnerPaymentHistoryListResponse() : LiveData<Resource<OwnerPaymentHistoryListResponse>> {
        return ownerPaymentHistoryListResponseLiveData
    }




}