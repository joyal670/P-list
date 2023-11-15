package com.property.propertyuser.ui.main.side_menu.refer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.referral.ReferralResponse
import com.property.propertyuser.modal.signup.SignUpResponse
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Resource
import kotlinx.coroutines.launch
import java.lang.Exception

class ReferAFriendViewModel:ViewModel() {

    private val referralResponseLiveData= MutableLiveData<Resource<ReferralResponse>>()

    fun fetchReferralCode( ){
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                referralResponseLiveData.postValue(Resource.loading(null))
                repository.showReferralCode().let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        referralResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        referralResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        referralResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }

                }
            }catch (ex: Exception){
                referralResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getReferralCode(): LiveData<Resource<ReferralResponse>> {
        return referralResponseLiveData
    }
}