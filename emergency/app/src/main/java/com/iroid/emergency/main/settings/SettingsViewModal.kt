package com.iroid.emergency.main.settings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.emergency.api.ApiRepositoryProvider
import com.iroid.emergency.modal.common.CommonResponse
import com.iroid.emergency.preference.AppPreferences
import com.iroid.emergency.start_up.utils.Resource
import kotlinx.coroutines.launch

class SettingsViewModal : ViewModel() {

    private val liveDataProfile = MutableLiveData<Resource<CommonResponse>>()
    val profileLiveData: LiveData<Resource<CommonResponse>> get() = liveDataProfile

    private val liveDataOtp = MutableLiveData<Resource<CommonResponse>>()
    val otpLiveData: LiveData<Resource<CommonResponse>> get() = liveDataOtp

    private val liveDataProfileUpdate = MutableLiveData<Resource<CommonResponse>>()
    val updateLiveData: LiveData<Resource<CommonResponse>> get() = liveDataProfileUpdate

    private val liveDataFaq = MutableLiveData<Resource<CommonResponse>>()
    val faqLiveData: LiveData<Resource<CommonResponse>> get() = liveDataFaq

    private val liveDataSubmission = MutableLiveData<Resource<CommonResponse>>()
    val submissionLiveData: LiveData<Resource<CommonResponse>> get() = liveDataSubmission

    private val liveDataFeedback = MutableLiveData<Resource<CommonResponse>>()
    val feedbackLiveData: LiveData<Resource<CommonResponse>> get() = liveDataFeedback

    fun feedbackUpdate(rating: Float, comments: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        liveDataFeedback.postValue(Resource.loading(null))
        viewModelScope.launch {
            try {
                repository.postFeedback(rating, comments).let {
                    val response = it.body()
                    if (response!!.status) {
                        liveDataFeedback.postValue(Resource.success(response))
                    }
                }
            } catch (ex: Exception) {
                liveDataFeedback.postValue(Resource.noInterNet("", null))
            }
        }
    }

    fun updateProfile(name: String, mobile: String) {
        if (isValidate(name, mobile)) {
            val repository = ApiRepositoryProvider.providerApiRepository()
            liveDataProfileUpdate.postValue(Resource.loading(null))
            viewModelScope.launch {
                try {
                    repository.profileUpdate(name, mobile).let {
                        val response = it.body()
                        if (response!!.status) {
                            liveDataProfileUpdate.postValue(Resource.success(response))
                        }else{
                            liveDataProfileUpdate.postValue(Resource.error(it.message(),null))
                        }
                    }
                } catch (ex: Exception) {
                    liveDataProfileUpdate.postValue(Resource.noInterNet("", null))
                }
            }
        }

    }

    fun getFaq() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        liveDataFaq.postValue(Resource.loading(null))
        viewModelScope.launch {
            try {
                repository.getFaq().let {
                    val response = it.body()
                    liveDataFaq.postValue(Resource.success(response))
                }
            } catch (ex: Exception) {
                liveDataFaq.postValue(Resource.noInterNet("", null))
            }
        }
    }

    fun updateSubmission(name: String, mobile: String, primaryType: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        liveDataSubmission.postValue(Resource.loading(null))
        viewModelScope.launch {
            try {
                repository.updateEmergencyProfile(name, mobile, primaryType).let {
                    val response = it.body()
                    if (response!!.status) {
                        liveDataSubmission.postValue(Resource.success(response))
                    } else {
                        liveDataSubmission.postValue(Resource.error(response.message, null))
                    }
                }
            } catch (ex: Exception) {
                liveDataSubmission.postValue(Resource.noInterNet("", null))
            }
        }
    }

    fun getProfile() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        liveDataProfile.postValue(Resource.loading(null))
        viewModelScope.launch {
            try {
                repository.getProfile().let {
                    val response = it.body()
                    if (response!!.status) {
                        liveDataProfile.postValue(Resource.success(response))
                    } else {
                        liveDataProfile.postValue(Resource.error(response.errors[0], null))
                    }
                }
            } catch (ex: Exception) {
                liveDataProfile.postValue(Resource.noInterNet("", null))
            }
        }
    }

    private fun isValidate(name: String, mobile: String): Boolean {
        return when {
            name.isEmpty() -> {
                liveDataProfileUpdate.postValue(Resource.error("Name is required", null))
                false
            }
            mobile.isEmpty() -> {
                liveDataProfileUpdate.postValue(Resource.error("Mobile number is required", null))
                false
            }
            else -> {
                true
            }
        }
    }
    fun verifyOtp(mobile:String,otp:String,fcm_token:String){
        if(isValidateOtp(AppPreferences.userMobile!!,otp)){
            val repository = ApiRepositoryProvider.providerApiRepository()
            liveDataOtp.postValue(Resource.loading(null))
            viewModelScope.launch {
                try {
                    repository.verifyOtpProfile(AppPreferences.userName!!,AppPreferences.userMobile!!,otp,).let {
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
                liveDataOtp.postValue(Resource.error("Mobile number is required",null))
                false
            }
            otp.isEmpty() -> {
                liveDataOtp.postValue(Resource.error("OTP is required",null))
                false
            }
            otp.length!=4-> {
                liveDataOtp.postValue(Resource.error("Invalid OTP",null))
                false
            }
            else -> {
                true
            }
        }
    }
}

