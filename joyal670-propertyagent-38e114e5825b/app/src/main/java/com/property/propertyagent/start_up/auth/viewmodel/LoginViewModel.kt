package com.property.propertyagent.start_up.auth.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.commen.login.LoginResponse
import com.property.propertyagent.modal.commen.logout.LogoutResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class LoginViewModel : ViewModel() {

    private val userLiveData = MutableLiveData<Resource<LoginResponse>>()
    private val logoutLiveData = MutableLiveData<Resource<LogoutResponse>>()

    val loginData: LiveData<Resource<LoginResponse>>
        get() = userLiveData

    val logout: MutableLiveData<Resource<LogoutResponse>>
        get() = logoutLiveData

    fun login(email: String, password: String, type: String, device_token: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                userLiveData.postValue(Resource.loading(null))
                repository.login(email, password, type, device_token).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        userLiveData.postValue(Resource.success(response))
                    } else {
                        userLiveData.postValue(Resource.error(response.response, response))
                    }
                }
            } catch (ex: UnknownHostException) {
                userLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )
            } catch (ex: NetworkErrorException) {
                userLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )
            } catch (ex: Exception) {
                userLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

    fun logout() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                logoutLiveData.postValue(Resource.loading(null))
                repository.logout().let {
                    if (it.isSuccessful) {
                        logoutLiveData.postValue(Resource.success(it.body()))
                    } else {
                        logoutLiveData.postValue(Resource.error(it.message(), it.body()))
                    }
                }
            } catch (ex: UnknownHostException) {
                logoutLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )
            } catch (ex: NetworkErrorException) {
                logoutLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )
            } catch (ex: Exception) {
                logoutLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }
}