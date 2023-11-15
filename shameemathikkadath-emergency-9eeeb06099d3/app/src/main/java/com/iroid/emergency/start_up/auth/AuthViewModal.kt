package com.iroid.emergency.start_up.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.emergency.modal.common.CommonResponse
import com.iroid.emergency.start_up.utils.Resource
import com.iroid.emergency.api.ApiRepositoryProvider
import kotlinx.coroutines.launch

class AuthViewModal : ViewModel() {
    private val liveDataSignUp = MutableLiveData<Resource<CommonResponse>>()
    val signUpLiveData: LiveData<Resource<CommonResponse>> get() = liveDataSignUp

    private val liveDataOtp = MutableLiveData<Resource<CommonResponse>>()
    val otpLiveData: LiveData<Resource<CommonResponse>> get() = liveDataOtp


    fun verifyOtp(mobile:String,otp:String,fcm_token:String){
        if(isValidateOtp(mobile,otp)){
            val repository = ApiRepositoryProvider.providerApiRepository()
            liveDataOtp.postValue(Resource.loading(null))
            viewModelScope.launch {
                try {
                    repository.verifyOtp(mobile,otp,fcm_token).let {
                        val  response=it.body()
                        if(response!!.status){
                            liveDataOtp.postValue(Resource.success(response))
                        }else{
                            liveDataOtp.postValue(Resource.error(response.errors[0],null))
                        }
                    }
                } catch (ex: Exception) {
                    liveDataOtp.postValue(Resource.noInterNet("", null))
                }
            }
        }
    }

    private fun isValidateOtp(mobile: String, otp: String): Boolean {
        Log.e("#TTTTT", "isValidateOtp: $otp" )
        return when {
            mobile.isEmpty() -> {
                liveDataSignUp.postValue(Resource.error("Mobile number is required",null))
                false
            }
            otp.isEmpty() -> {
                liveDataSignUp.postValue(Resource.error("OTP is required",null))
                false
            }
            otp.length!=4-> {
                liveDataSignUp.postValue(Resource.error("Invalid OTP",null))
                false
            }
            else -> {
                true
            }
        }
    }

    fun register(
        name: String,
        mobile: String
    ) {
        if(isValidate(name,mobile)){
            val repository = ApiRepositoryProvider.providerApiRepository()
            liveDataSignUp.postValue(Resource.loading(null))
            viewModelScope.launch {
                try {
                    repository.register(name,mobile.toLong()).let {
                        val  response=it.body()
                        if(response!!.status){
                            liveDataSignUp.postValue(Resource.success(response))
                        }else{
                            liveDataSignUp.postValue(Resource.error(response.errors[0],null))
                        }
                    }
                } catch (ex: Exception) {
                    liveDataSignUp.postValue(Resource.noInterNet("", null))
                }
            }
        }

    }

    private fun isValidate(name: String, mobile: String): Boolean {
        return when {
            name.isEmpty() -> {
                liveDataSignUp.postValue(Resource.error("Name is required",null))
                false
            }
            mobile.isEmpty() -> {
                liveDataSignUp.postValue(Resource.error("Mobile number is required",null))
                false
            }
            else -> {
                true
            }
        }
    }
}
