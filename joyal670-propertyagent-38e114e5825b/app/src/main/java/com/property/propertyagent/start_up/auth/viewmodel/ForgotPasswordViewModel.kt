package com.property.propertyagent.start_up.auth.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.commen.forgot_password.ForgotPasswordResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class ForgotPasswordViewModel : ViewModel() {

    private val forgotPasswordLiveData = MutableLiveData<Resource<ForgotPasswordResponse>>()

    val forgotPasswordData: LiveData<Resource<ForgotPasswordResponse>>
        get() = forgotPasswordLiveData

    fun forgotPassword(email: String, user_type: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                forgotPasswordLiveData.postValue(Resource.loading(null))
                repository.forgotPassword(email, user_type).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        forgotPasswordLiveData.postValue(Resource.success(it.body()))
                    } else {
                        forgotPasswordLiveData.postValue(
                            Resource.error(
                                response.response,
                                it.body()
                            )
                        )
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