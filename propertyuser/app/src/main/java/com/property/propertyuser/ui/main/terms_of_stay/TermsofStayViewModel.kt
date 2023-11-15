package com.property.propertyuser.ui.main.terms_of_stay

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.terms_of_stay.TermsOfStayResponse
import com.property.propertyuser.modal.user_notification.NotificationResponse
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Resource
import kotlinx.coroutines.launch

class TermsofStayViewModel: ViewModel() {

    private val termsOfStayResponseLiveData= MutableLiveData<Resource<TermsOfStayResponse>>()

    fun fetchTermsOfStay(property_id:String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            termsOfStayResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.getTermsOfStay(property_id).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        termsOfStayResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        termsOfStayResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        termsOfStayResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                termsOfStayResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getTermsOfStayResponse(): LiveData<Resource<TermsOfStayResponse>> {
        return termsOfStayResponseLiveData
    }
}