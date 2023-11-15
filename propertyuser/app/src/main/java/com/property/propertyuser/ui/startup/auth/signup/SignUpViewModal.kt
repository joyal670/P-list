package com.property.propertyuser.ui.startup.auth.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.google_login.GoogleLoginResponse
import com.property.propertyuser.modal.signup.SignUpResponse
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Resource
import com.property.propertyuser.utils.Resource.Companion.loading
import kotlinx.coroutines.launch
import java.lang.Exception

class SignUpViewModal :ViewModel(){
    private val signUpResponseLiveData=MutableLiveData<Resource<SignUpResponse>>()

    fun addSignUp( phone: String,
                   email: String,
                   name: String,
                   lat: String,
                   lan: String,location:String,referral:String){
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
               signUpResponseLiveData.postValue(Resource.loading(null))
                repository.signUp(phone, email, name, lat, lan,location,referral).let {
                    val response=it.body()
                    if(response!!.status==Constants.REQUEST_OK){
                        signUpResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status==Constants.REQUEST_BAD_REQUEST){
                        signUpResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status==Constants.REQUEST_UNAUTHORIZED){
                        signUpResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }

                }
            }catch (ex:Exception){
                signUpResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getSignUpData():LiveData<Resource<SignUpResponse>>{
        return signUpResponseLiveData
    }

    private val googleLoginResponseLiveData=MutableLiveData<Resource<GoogleLoginResponse>>()

    fun fetchGoogleLogin(name: String, email: String, phone: String, google_id: String,
                   facebook_id: String, apple_id: String, login_type: String){
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                googleLoginResponseLiveData.postValue(Resource.loading(null))
                repository.googleLogin(name,email,phone,google_id,facebook_id,apple_id,login_type).let {
                    val response=it.body()
                    if(response!!.status==Constants.REQUEST_OK){
                        googleLoginResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status==Constants.REQUEST_BAD_REQUEST){
                        googleLoginResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status==Constants.REQUEST_UNAUTHORIZED){
                        googleLoginResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }

                }
            }catch (ex:Exception){
                googleLoginResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getGoogleLoginData():LiveData<Resource<GoogleLoginResponse>>{
        return googleLoginResponseLiveData
    }

}