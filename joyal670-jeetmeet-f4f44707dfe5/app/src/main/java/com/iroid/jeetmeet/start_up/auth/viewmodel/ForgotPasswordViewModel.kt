package com.iroid.jeetmeet.start_up.auth.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.forgot_password.ForgotPasswordResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class ForgotPasswordViewModel : ViewModel() {
    /* variables */
    private val forgotPasswordLiveData = MutableLiveData<Resource<ForgotPasswordResponse>>()

    val forgotPasswordData: LiveData<Resource<ForgotPasswordResponse>>
        get() = forgotPasswordLiveData

    /* Forgot password */
    fun forgotPassword(email: String, user_type: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                forgotPasswordLiveData.postValue(Resource.loading(null))
                repository.forgotPassword(email, user_type).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        forgotPasswordLiveData.postValue(Resource.success(response))
                    } else {
                        forgotPasswordLiveData.postValue(Resource.error(response.message, response))
                    }

                }
            } catch (ex: UnknownHostException) {
                forgotPasswordLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                forgotPasswordLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                forgotPasswordLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }
}