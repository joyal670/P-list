package com.property.propertyuser.ui.startup.auth.otp

/*import com.property.propertyuser.data.api.ApiHelper*/
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.otp.OtpResponse
import com.property.propertyuser.modal.signup.SignUpResponse
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Constants.REQUEST_BAD_REQUEST
import com.property.propertyuser.utils.Constants.REQUEST_OK
import com.property.propertyuser.utils.Resource
import kotlinx.coroutines.launch

class OtpViewModel : ViewModel() {
    private val otpResponseLiveData = MutableLiveData<Resource<OtpResponse>>()

    fun otpVerify(phone: String, otp: String, device_token: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                otpResponseLiveData.postValue(Resource.loading(null))
                repository.login(phone, otp, device_token).let {
                    val response = it.body()
                    if (response!!.status == REQUEST_OK) {
                        otpResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == REQUEST_BAD_REQUEST) {
                        otpResponseLiveData.postValue(Resource.dataEmpty("", response))

                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        otpResponseLiveData.postValue(Resource.dataEmpty("", response))
                    }
                }
            } catch (ex: Exception) {
                otpResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getOtpLiveData(): MutableLiveData<Resource<OtpResponse>> {
        return otpResponseLiveData
    }

    private val reSendOtpResponseLiveData = MutableLiveData<Resource<SignUpResponse>>()

    fun reSendOtp(phone: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                reSendOtpResponseLiveData.postValue(Resource.loading(null))
                repository.phoneVerificationResendOtp(phone).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        reSendOtpResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        reSendOtpResponseLiveData.postValue(Resource.dataEmpty("null", response))
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        reSendOtpResponseLiveData.postValue(Resource.dataEmpty("null", response))
                    }

                }
            } catch (ex: Exception) {
                reSendOtpResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getResendOtpResponse(): LiveData<Resource<SignUpResponse>> {
        return reSendOtpResponseLiveData
    }

}