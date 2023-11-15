package com.iroid.patrickstore.ui.forgotpassword.otp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.patrickstore.api.ApiRepositoryProvider
import com.iroid.patrickstore.model.otp.OtpResponse
import com.iroid.patrickstore.utils.Constants
import com.iroid.patrickstore.utils.Resource
import kotlinx.coroutines.launch

class OtpViewModel : ViewModel() {
    private val liveDataOtp = MutableLiveData<Resource<OtpResponse>>()
    val otpLiveData: LiveData<Resource<OtpResponse>> get() = liveDataOtp

    fun otpVerify(otp: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                liveDataOtp.postValue(Resource.loading(null))
                repository.otpVerify(otp).let {
                    val response = it.body()
                    when (response!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            liveDataOtp.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveDataOtp.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveDataOtp.postValue(Resource.error(response.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveDataOtp.postValue(Resource.noInterNet("", null))
                        }
                    }
                }
            } catch (ex: Exception) {
                liveDataOtp.postValue(Resource.noInterNet("", null))
            }
        }

    }


}