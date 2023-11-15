package com.iroid.jeetmeet.start_up.auth.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.parent.login.ParentLoginResponse
import com.iroid.jeetmeet.modal.parent.logout.ParentLogoutResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class ParentLoginViewModel : ViewModel() {
    /* variables */
    private val parentLoginLiveData = MutableLiveData<Resource<ParentLoginResponse>>()
    private val parentLogoutLiveData = MutableLiveData<Resource<ParentLogoutResponse>>()

    val parentLoginData: LiveData<Resource<ParentLoginResponse>>
        get() = parentLoginLiveData

    val parentLogoutData: LiveData<Resource<ParentLogoutResponse>>
        get() = parentLogoutLiveData

    /* Parent login */
    fun parentLoginApi(email: String, password: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                parentLoginLiveData.postValue(Resource.loading(null))
                repository.parentLogin(email, password).let {
                    val response = it.body()
                    if (it.isSuccessful) {
                        parentLoginLiveData.postValue(Resource.success(response))
                    } else {
                        if (it.code() == Constants.REQUEST_BAD_REQUEST) {
                            parentLoginLiveData.postValue(
                                Resource.error(
                                    "The username must be a valid email address",
                                    response
                                )
                            )
                        }
                        if (it.code() == Constants.REQUEST_UNAUTHORIZED) {
                            parentLoginLiveData.postValue(Resource.error("Unauthorized", response))
                        }
                    }
                }
            } catch (ex: UnknownHostException) {
                parentLoginLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                parentLoginLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                parentLoginLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

    /* Parent logout */
    fun parentLogoutApi() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                parentLogoutLiveData.postValue(Resource.loading(null))
                repository.parentLogout().let {
                    val response = it.body()
                    if (it.isSuccessful) {
                        parentLogoutLiveData.postValue(Resource.success(response))
                    } else {
                        parentLogoutLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                parentLogoutLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                parentLogoutLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                parentLogoutLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }
}