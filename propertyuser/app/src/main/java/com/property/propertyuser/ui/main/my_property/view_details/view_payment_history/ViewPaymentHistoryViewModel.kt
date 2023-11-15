package com.property.propertyuser.ui.main.my_property.view_details.view_payment_history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.CommonResponse
import com.property.propertyuser.modal.payment_history.PaymentHistoryResponse
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Resource
import kotlinx.coroutines.launch
import java.math.BigInteger

class ViewPaymentHistoryViewModel :ViewModel(){
    private val paymentHistoryResponseLiveData=
        MutableLiveData<Resource<PaymentHistoryResponse>>()

    fun paymentHistory(page: String,user_property_id: String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            paymentHistoryResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.userPaymentHistory(page,user_property_id).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        paymentHistoryResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        paymentHistoryResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        paymentHistoryResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                paymentHistoryResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getPaymentHistoryResponse(): LiveData<Resource<PaymentHistoryResponse>> {
        return paymentHistoryResponseLiveData
    }
}