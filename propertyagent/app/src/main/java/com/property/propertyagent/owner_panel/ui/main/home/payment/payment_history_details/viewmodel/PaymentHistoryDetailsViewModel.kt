package com.property.propertyagent.owner_panel.ui.main.home.payment.payment_history_details.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.owner.owner_payment_history_details.OwnerPaymentHistoryDetailsResponse
import com.property.propertyagent.modal.owner.owner_payment_recevied.OwnerPaymentReceviedResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch

class PaymentHistoryDetailsViewModel : ViewModel() {

    private val paymentHistoryDetailsResponseLiveData =
        MutableLiveData<Resource<OwnerPaymentHistoryDetailsResponse>>()

    private val paymentReceivedResponseLiveData =
        MutableLiveData<Resource<OwnerPaymentReceviedResponse>>()

    /* payment details api */
    fun paymentHistoryDetails(payment_id : Int , property_id : Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            paymentHistoryDetailsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.ownerPaymentHistoryDetails(payment_id , property_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        paymentHistoryDetailsResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        paymentHistoryDetailsResponseLiveData.postValue(
                            Resource.error(
                                "null" ,
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        paymentHistoryDetailsResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null" ,
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        paymentHistoryDetailsResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null" ,
                                response
                            )
                        )
                    }
                }

            } catch (ex : Exception) {
                paymentHistoryDetailsResponseLiveData.postValue(Resource.noInternet("" , null))
                Log.e("TAG" , "paymentListOfProperties: $ex")
            }
        }
    }

    /* property details */
    fun getPaymentHistoryDetailsResponse() : LiveData<Resource<OwnerPaymentHistoryDetailsResponse>> {
        return paymentHistoryDetailsResponseLiveData
    }


    /* payment received api */
    fun paymentReceived(property_id : Int , payment_id : Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            paymentReceivedResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.ownerPaymentReceived(property_id , payment_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        paymentReceivedResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        paymentReceivedResponseLiveData.postValue(
                            Resource.error(
                                "null" ,
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        paymentReceivedResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null" ,
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        paymentReceivedResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null" ,
                                response
                            )
                        )
                    }
                }

            } catch (ex : Exception) {
                paymentReceivedResponseLiveData.postValue(Resource.noInternet("" , null))
                Log.e("TAG" , "paymentListOfProperties: $ex")
            }
        }
    }

    /* property details */
    fun getPaymentReceivedResponse() : LiveData<Resource<OwnerPaymentReceviedResponse>> {
        return paymentReceivedResponseLiveData
    }

}