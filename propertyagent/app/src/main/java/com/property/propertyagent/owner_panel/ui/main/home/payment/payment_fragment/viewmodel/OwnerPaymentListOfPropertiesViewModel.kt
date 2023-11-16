package com.property.propertyagent.owner_panel.ui.main.home.payment.payment_fragment.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.owner.owner_payment_list_of_properties.OwnerPaymentListOfPropertiesResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch

class OwnerPaymentListOfPropertiesViewModel : ViewModel()
{
    private val ownerPaymentResponseLiveData= MutableLiveData<Resource<OwnerPaymentListOfPropertiesResponse>>()

    fun paymentListOfProperties(property_to : Int, page : String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            ownerPaymentResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.ownerPaymentListOfProperties(property_to, page).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        ownerPaymentResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response.status== Constants.INTERNAL_SERVER_ERROR){
                        ownerPaymentResponseLiveData.postValue(Resource.error("null",response))
                    }
                    if(response.status== Constants.REQUEST_BAD_REQUEST){
                        ownerPaymentResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response.status== Constants.REQUEST_UNAUTHORIZED){
                        ownerPaymentResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                ownerPaymentResponseLiveData.postValue(Resource.noInternet("",null))
                Log.e("TAG" , "paymentListOfProperties: $ex"  )
            }
        }
    }

    fun getOwnerPaymentResponse(): LiveData<Resource<OwnerPaymentListOfPropertiesResponse>> {
        return ownerPaymentResponseLiveData
    }
}