package com.iroid.healthdomain.ui.login

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.iroid.healthdomain.data.model_class.GenerateOtp
import com.iroid.healthdomain.data.model_class.otpValidation.ValidateOtpResponse
import com.iroid.healthdomain.data.network.Resource
import com.iroid.healthdomain.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class LoginViewModel(val repository: LoginRepository, application: Application) : BaseViewModel(application) {

    val getOTPResponse: LiveData<Resource<GenerateOtp>> = MutableLiveData()
    val validateOTPResponse: LiveData<Resource<ValidateOtpResponse>> = MutableLiveData()
    val reSendOTPResponse: LiveData<Resource<GenerateOtp>> = MutableLiveData()


    fun generateOtp(number: String) = viewModelScope.launch {
        getOTPResponse as MutableLiveData
        getOTPResponse.value = Resource.Loading
        getOTPResponse.value = repository.apiToGenerate(number)
    }


    fun checkOptValue(otpValue: String, phone: String) = viewModelScope.launch {

        validateOTPResponse as MutableLiveData
        validateOTPResponse.value = Resource.Loading
        validateOTPResponse.value = repository.validateOtp(otpValue, phone)

    }


    fun reSendOtp(number: String) = viewModelScope.launch {
        reSendOTPResponse as MutableLiveData
        reSendOTPResponse.value = Resource.Loading
        reSendOTPResponse.value = repository.reSendOtp(number)
    }


    suspend fun saveAuthToken(token: String) {
        repository.saveAuthToken(token)
    }

    suspend fun savePhoneNumber(number: String) {
        repository.saveNumber(number)
    }


    suspend fun setUserStatus(value: Boolean) {
        repository.saveUserStatus(value)
    }


}