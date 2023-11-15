package com.property.propertyuser.ui.main.side_menu.become_owner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.CommonResponse
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Resource
import kotlinx.coroutines.launch
import java.math.BigInteger

class BecomeOwnerViewModel: ViewModel() {

    private val becomeOwnerResponseLiveData=
        MutableLiveData<Resource<CommonResponse>>()

    fun becomeOwner(name: String, phone: String, email: String,
                    no_of_rental_properties: String, no_of_sale_properties: String, property_rel: String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            becomeOwnerResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.becomeAnOwner(name,phone,email, no_of_rental_properties,
                    no_of_sale_properties,property_rel).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        becomeOwnerResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        becomeOwnerResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        becomeOwnerResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                becomeOwnerResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getBecomeOwnerResponse(): LiveData<Resource<CommonResponse>> {
        return becomeOwnerResponseLiveData
    }
}