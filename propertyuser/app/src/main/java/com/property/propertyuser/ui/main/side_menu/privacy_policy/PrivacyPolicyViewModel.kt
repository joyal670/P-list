package com.property.propertyuser.ui.main.side_menu.privacy_policy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.CommonResponse
import com.property.propertyuser.modal.faq.FAQResponse
import com.property.propertyuser.modal.privacy_policy.PrivacyPolicyResponse
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Resource
import kotlinx.coroutines.launch

class PrivacyPolicyViewModel:ViewModel() {
    private val privacyPolicyListDetailsResponseLiveData= MutableLiveData<Resource<PrivacyPolicyResponse>>()

    fun fetchPrivacyPolicyListDetail(page:String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            privacyPolicyListDetailsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.privacyPolicyListDetails(page).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        privacyPolicyListDetailsResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.INTERNAL_SERVER_ERROR){
                        privacyPolicyListDetailsResponseLiveData.postValue(Resource.error("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        privacyPolicyListDetailsResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        privacyPolicyListDetailsResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                privacyPolicyListDetailsResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getPrivacyPolicyListDetailsResponse(): LiveData<Resource<PrivacyPolicyResponse>> {
        return privacyPolicyListDetailsResponseLiveData
    }

}